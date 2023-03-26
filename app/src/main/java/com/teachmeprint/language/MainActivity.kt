package com.teachmeprint.language

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.checkSelfPermission
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.teachmeprint.language.data.service.ScreenShotService
import com.teachmeprint.language.databinding.ActivityMainBinding
import com.teachmeprint.language.core.util.snackBarAlert

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
        if (hasOverlayPermission() && hasPermissions()) {
            startService()
        } else {
            binding.linearMainContainer.snackBarAlert(R.string.text_message_alert_permission)
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
        setupScreenShotService()
    }

    private fun setupScreenShotService() {
        Intent(this, ScreenShotService::class.java).also {
            startService(it)
        }.run {
            finish()
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
        PERMISSIONS.all {
            checkSelfPermission(this, it) == PERMISSION_GRANTED
        }

    private fun hasOverlayPermission() =
        Settings.canDrawOverlays(this)


    companion object {
        private const val SCHEME_PACKAGE = "package"
        private val PERMISSIONS = arrayOf(
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE
        )
    }
}
