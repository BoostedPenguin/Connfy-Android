package com.industryproject.connfy.networkManager

import android.app.Application
import com.industryproject.connfy.networkManager.NetworkManager

class ConnfyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NetworkManager.getInstance(this)
    }
}