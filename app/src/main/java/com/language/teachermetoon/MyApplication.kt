package com.language.teachermetoon

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.language.teachermetoon.core.di.module.addModule
import com.orhanobut.hawk.Hawk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class MyApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MyApplication? = null
        fun applicationContext() = instance as MyApplication
        const val CHANNEL_ID = "teach_me_toon_notification"
    }

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        setupNotificationChannel()
        setupHawk()
        setupTimber()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@MyApplication)
            modules(addModule)
        }
    }

    private fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Teach Me Toon"
            val channelDescription = "Language Learning"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
                description = channelDescription
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
}