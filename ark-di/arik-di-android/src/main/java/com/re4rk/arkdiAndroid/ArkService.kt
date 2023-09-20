package com.re4rk.arkdiAndroid

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.re4rk.arkdi.ArkModule

abstract class ArkService : Service() {
    private val arkModule: ArkModule by lazy { (application as ArkApplication).getServiceModule() }

    override fun onCreate() {
        super.onCreate()
        arkModule.inject(this)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
