package com.re4rk.arkdiAndroid

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.re4rk.arkdi.DiContainer

abstract class DiApplication : Application() {
    abstract val diGenerator: DiGenerator

    private val diContainer: DiContainer
        by lazy { diGenerator.createApplicationModule(applicationContext) }

    override fun onCreate() {
        super.onCreate()
        diContainer.inject(this)
    }

    fun getActivityDiContainer(activity: ComponentActivity): DiViewModel {
        return ViewModelProvider(activity)[DiViewModel::class.java].apply {
            if (isInitialized.not()) {
                ownerRetainedDiContainer =
                    diGenerator.createRetainedActivityModule(diContainer, activity)
                viewModelDiContainer = diGenerator.createViewModelModule(ownerRetainedDiContainer)
            }
            ownerDiContainer = diGenerator.createActivityModule(ownerRetainedDiContainer, activity)
        }
    }
}
