package com.teachmeprint.language

import android.Manifest.permission.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.teachmeprint.common.util.snackBarAlert
import com.teachmeprint.language.databinding.ActivityMainBinding
import com.teachmeprint.screencapture.service.ScreenShotService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            if (!it.value) {
                tryAgainReadAndWritePermission()
                return@registerForActivityResult
            }
        }
    }

    private val registerForActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (!hasOverlayPermission() && !hasPermissions()) {
            binding.linearMainContainer.snackBarAlert(R.string.text_message_alert_permission)
        }
    }

    private val requestScreenShotService = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            lifecycleScope.launch {
                setupScreenShotService(result.data)
                withContext(Dispatchers.Main) {
                    finish()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.textButton.setOnClickListener {
            startService()
        }
    }

    private fun startService() {
        setupPermissions()
    }

    private fun setupPermissions() {
        if (!hasPermissions()) {
            requestMultiplePermissions.launch(PERMISSIONS)
            return
        }

        if (!hasOverlayPermission()) {
            requestOverlayPermission()
            return
        }
        launchMediaProjectionManagerPermission()
    }

    private fun setupScreenShotService(resultData: Intent?) {
        ScreenShotService.getStartIntent(this, resultData).also {
            startService(it)
        }
    }

    private fun requestOverlayPermission() {
        if (!hasOverlayPermission()) {
            setupDialogPermission(
                title = R.string.text_title_display_dialog_permission,
                message = R.string.text_message_display_dialog_permission
            ) {
                launchOverlayPermission()
            }
        } else {
            startService()
        }
    }

    private fun launchOverlayPermission() {
        val intent = Intent(ACTION_MANAGE_OVERLAY_PERMISSION)
        registerForActivityResult.launch(intent)
    }

    private fun tryAgainReadAndWritePermission() {
        setupDialogPermission(
            title = R.string.text_title_read_dialog_permission,
            message = R.string.text_message_read_dialog_permission
        ) {
            launchActionApplicationDetailsSettings()
        }
    }

    private fun launchActionApplicationDetailsSettings() =
        with(Intent(ACTION_APPLICATION_DETAILS_SETTINGS)) {
            val uri = Uri.fromParts(SCHEME_PACKAGE, packageName, null)
            data = uri
            registerForActivityResult.launch(this)
        }

    private fun launchMediaProjectionManagerPermission() {
        val mediaProjectionManager: MediaProjectionManager =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        requestScreenShotService.launch(mediaProjectionManager.createScreenCaptureIntent())
    }

    private fun setupDialogPermission(
        @StringRes title: Int,
        @StringRes message: Int,
        block: () -> Unit
    ) {
        MaterialAlertDialogBuilder(this).apply {
            setTitle(getString(title))
            setMessage(getString(message))
                .setPositiveButton(getString(R.string.text_button_settings_dialog_permission)) { dialog, _ ->
                    block.invoke()
                    dialog.dismiss()
                }
        }.show()
    }

    private fun hasPermissions() =
        PERMISSIONS.filter { it != WRITE_EXTERNAL_STORAGE }.all {
            checkSelfPermission(this, it) == PERMISSION_GRANTED
        }

    private fun hasOverlayPermission() =
        Settings.canDrawOverlays(this)

    companion object {
        private const val SCHEME_PACKAGE = "package"
        private val PERMISSIONS = arrayOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                READ_MEDIA_IMAGES
            } else {
                READ_EXTERNAL_STORAGE
            },
            WRITE_EXTERNAL_STORAGE
        )
    }
}
