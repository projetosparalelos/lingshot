package com.teachmeprint.language

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.os.Build
import com.google.android.gms.ads.MobileAds
import com.teachmeprint.language.core.di.module.addModule
import com.orhanobut.hawk.Hawk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class TeachMePrintApplication : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        setupNotificationChannel()
        setupHawk()
        setupTimber()
        setupMobileAds()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@TeachMePrintApplication)
            modules(addModule)
        }
    }

    private fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_DEFAULT).apply {
                description = CHANNEL_DESCRIPTION
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setupHawk() {
        Hawk.init(this).build()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setupMobileAds() {
        MobileAds.initialize(this) {
            Timber.d(it.toString())
        }
    }

    companion object {
        private var instance: TeachMePrintApplication? = null
        fun applicationContext() = instance as TeachMePrintApplication

        const val CHANNEL_ID = "teach_me_print_notification"
        private const val CHANNEL_NAME = "Teach Me Print"
        private const val CHANNEL_DESCRIPTION = "Language Learning"
    }
}