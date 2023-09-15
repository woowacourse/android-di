package com.re4rk.arkdiAndroid

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.re4rk.arkdi.ArkContainer
import com.re4rk.arkdiAndroid.arkModules.ArkModules

abstract class ArkApplication : Application() {
    abstract val arkModules: ArkModules

    private val arkContainer: ArkContainer
        by lazy { arkModules.createApplicationModule(applicationContext) }

    override fun onCreate() {
        super.onCreate()
        arkContainer.inject(this)
    }

    fun getActivityDiContainer(activity: ComponentActivity): ArkViewModel {
        return ViewModelProvider(activity)[ArkViewModel::class.java].apply {
            if (isInitialized.not()) {
                ownerRetainedArkContainer =
                    arkModules.createRetainedActivityModule(arkContainer, activity)
                viewModelArkContainer = arkModules.createViewModelModule(ownerRetainedArkContainer)
            }
            ownerArkContainer = arkModules.createActivityModule(ownerRetainedArkContainer, activity)
        }
    }
}
