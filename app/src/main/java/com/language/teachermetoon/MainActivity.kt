package com.language.teachermetoon

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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.checkSelfPermission
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.language.teachermetoon.data.service.ScreenShotService
import com.language.teachermetoon.databinding.ActivityMainBinding
import com.language.teachermetoon.core.util.snackBarAlert

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            if (!it.value) {
                tryAgainPermissionReadAndWrite()
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
            binding.linearMainContainer.snackBarAlert("Permission is required.")
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
            val intent = Intent(ACTION_MANAGE_OVERLAY_PERMISSION)
            registerForActivityResult.launch(intent)
        } else {
            startService()
        }
    }

    private fun tryAgainPermissionReadAndWrite() {
        MaterialAlertDialogBuilder(this).apply {
            setMessage("This app needs you to allow this permission in order to function." +
                "\n\nWill you allow it?")
                .setPositiveButton("Yes") { _, _ ->
                    setupActionApplicationDetailsSettings()
                }
                .setNegativeButton("No") { _, _ -> }
        }.show()
    }

    private fun setupActionApplicationDetailsSettings() =
        with(Intent(ACTION_APPLICATION_DETAILS_SETTINGS)) {
            val uri = Uri.fromParts("package", packageName, null)
            data = uri
            registerForActivityResult.launch(this)
        }


    private fun hasPermissions() =
        PERMISSIONS.all {
            checkSelfPermission(this, it) == PERMISSION_GRANTED
        }

    private fun hasOverlayPermission()  =
        Settings.canDrawOverlays(this)


    companion object {
        private val PERMISSIONS = arrayOf(
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE
        )
    }
}
