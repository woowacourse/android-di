package com.re4rk.arkdiAndroid

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.re4rk.arkdi.ArkContainer
import com.re4rk.arkdiAndroid.arkGenerator.ArkGenerator

abstract class ArkApplication : Application() {
    abstract val arkGenerator: ArkGenerator

    private val arkContainer: ArkContainer
        by lazy { arkGenerator.createApplicationModule(applicationContext) }

    override fun onCreate() {
        super.onCreate()
        arkContainer.inject(this)
    }

    fun getActivityDiContainer(activity: ComponentActivity): ArkViewModel {
        return ViewModelProvider(activity)[ArkViewModel::class.java].apply {
            if (isInitialized.not()) {
                ownerRetainedArkContainer =
                    arkGenerator.createRetainedActivityModule(arkContainer, activity)
                viewModelArkContainer = arkGenerator.createViewModelModule(ownerRetainedArkContainer)
            }
            ownerArkContainer = arkGenerator.createActivityModule(ownerRetainedArkContainer, activity)
        }
    }
}
