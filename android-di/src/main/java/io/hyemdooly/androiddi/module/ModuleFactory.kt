package io.hyemdooly.androiddi.module

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import io.hyemdooly.androiddi.base.HyemdoolyViewModel
import io.hyemdooly.di.Module

class ModuleFactory(private val modules: Modules) {
    fun createApplicationModule(applicationContext: Context) =
        modules.applicationModule(applicationContext)

    fun createViewModelWithApplicationModule(
        applicationModule: Module,
        activity: ComponentActivity,
    ): HyemdoolyViewModel {
        return ViewModelProvider(activity)[HyemdoolyViewModel::class.java].apply {
            activityModule = modules.activityModule(applicationModule, activity)
            viewModelModule = modules.activityModule(applicationModule, activity)
        }
    }
}
