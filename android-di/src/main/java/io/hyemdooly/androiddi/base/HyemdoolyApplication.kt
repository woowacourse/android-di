package io.hyemdooly.androiddi.base

import android.app.Application
import androidx.activity.ComponentActivity
import io.hyemdooly.androiddi.module.ModuleFactory
import io.hyemdooly.androiddi.module.Modules
import io.hyemdooly.di.Module

abstract class HyemdoolyApplication(modules: Modules) : Application() {
    private val moduleFactory by lazy { ModuleFactory(modules) }
    private val applicationModule: Module by lazy {
        moduleFactory.createApplicationModuleOf(
            applicationContext,
        )
    }

    override fun onCreate() {
        super.onCreate()
        applicationModule.injectFields(this)
    }

    fun getHyemdoolyViewModel(activity: ComponentActivity): HyemdoolyViewModel {
        return moduleFactory.createViewModelOf(applicationModule, activity)
    }
}
