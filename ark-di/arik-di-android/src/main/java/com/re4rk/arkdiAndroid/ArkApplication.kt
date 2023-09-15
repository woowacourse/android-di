package com.re4rk.arkdiAndroid

import android.app.Application
import androidx.activity.ComponentActivity
import com.re4rk.arkdi.ArkContainer
import com.re4rk.arkdiAndroid.arkModules.ArkModules

abstract class ArkApplication(arkModules: ArkModules) : Application() {
    private val arkContainerFactory: ArkContainerFactory by lazy { ArkContainerFactory(arkModules) }

    private val arkContainer: ArkContainer
        by lazy { arkModules.createApplicationModule(applicationContext) }

    override fun onCreate() {
        super.onCreate()
        arkContainerFactory.createApplicationContainer(applicationContext)
        arkContainer.inject(this)
    }

    fun getActivityDiContainer(activity: ComponentActivity): ArkViewModel {
        return arkContainerFactory.createActivityContainer(arkContainer, activity)
    }
}
