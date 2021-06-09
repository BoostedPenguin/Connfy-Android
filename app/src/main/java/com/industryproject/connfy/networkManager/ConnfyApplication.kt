package com.industryproject.connfy.networkManager

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ConnfyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkManager.getInstance(this)
    }
}