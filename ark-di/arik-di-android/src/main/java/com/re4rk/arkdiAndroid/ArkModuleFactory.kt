package com.re4rk.arkdiAndroid

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.re4rk.arkdi.ArkModule
import com.re4rk.arkdiAndroid.arkModules.ArkModules

internal class ArkModuleFactory(private val arkModules: ArkModules) {
    fun createApplicationModule(applicationContext: Context) {
        arkModules.createApplicationModule(applicationContext)
    }

    fun createActivityModule(parentModule: ArkModule, activity: ComponentActivity): ArkViewModel {
        return ViewModelProvider(activity)[ArkViewModel::class.java].apply {
            if (isInitialized.not()) {
                ownerRetainedModule =
                    arkModules.createRetainedActivityModule(parentModule, activity)
                viewModelModule = arkModules.createViewModelModule(ownerRetainedModule)
            }
            ownerModule = arkModules.createActivityModule(ownerRetainedModule, activity)
        }
    }
}
