package com.re4rk.arkdiAndroid

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.re4rk.arkdi.ArkModule
import com.re4rk.arkdiAndroid.arkModules.ArkModules

internal class ArkModuleFactory(private val arkModules: ArkModules) {
    fun createApplicationModule(applicationContext: Context): ArkModule {
        return arkModules.applicationModule(applicationContext)
    }

    fun createActivityModule(parentModule: ArkModule, activity: ComponentActivity): ArkViewModel {
        return ViewModelProvider(activity)[ArkViewModel::class.java].apply {
            if (isInitialized.not()) {
                ownerRetainedModule = arkModules.retainedActivityModule(parentModule, activity)
                viewModelModule = arkModules.viewModelModule(ownerRetainedModule)
            }
            ownerModule = arkModules.activityModule(ownerRetainedModule, activity)
        }
    }

    fun createServiceModule(arkModule: ArkModule): ArkModule {
        return arkModules.serviceModule(arkModule)
    }
}
