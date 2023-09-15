package com.re4rk.arkdiAndroid

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.re4rk.arkdi.ArkContainer
import com.re4rk.arkdiAndroid.arkModules.ArkModules

internal class ArkContainerFactory(private val arkModules: ArkModules) {
    fun createApplicationContainer(applicationContext: Context) {
        arkModules.createApplicationModule(applicationContext)
    }

    fun createActivityContainer(parent: ArkContainer, activity: ComponentActivity): ArkViewModel {
        return ViewModelProvider(activity)[ArkViewModel::class.java].apply {
            if (isInitialized.not()) {
                ownerRetainedArkContainer = arkModules.createRetainedActivityModule(parent, activity)
                viewModelArkContainer = arkModules.createViewModelModule(ownerRetainedArkContainer)
            }
            ownerArkContainer = arkModules.createActivityModule(ownerRetainedArkContainer, activity)
        }
    }
}
