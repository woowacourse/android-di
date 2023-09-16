package com.re4rk.arkdiAndroid

import android.app.Application
import androidx.activity.ComponentActivity
import com.re4rk.arkdi.ArkModule
import com.re4rk.arkdiAndroid.arkModules.ArkModules

abstract class ArkApplication(arkModules: ArkModules) : Application() {
    private val arkModuleFactory: ArkModuleFactory = ArkModuleFactory(arkModules)

    private val arkModule: ArkModule
        by lazy { arkModuleFactory.createApplicationModule(applicationContext) }

    override fun onCreate() {
        super.onCreate()
        arkModule.inject(this)
    }

    fun getActivityModule(activity: ComponentActivity): ArkViewModel {
        return arkModuleFactory.createActivityModule(arkModule, activity)
    }

    fun getServiceModule(): ArkModule {
        return arkModuleFactory.createServiceModule(arkModule)
    }
}
