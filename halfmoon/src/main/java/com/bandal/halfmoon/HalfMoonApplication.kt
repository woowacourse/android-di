package com.bandal.halfmoon

import android.app.Application
import com.bandal.fullmoon.AppContainer
import com.bandal.fullmoon.FullMoonInjector

open class HalfMoonApplication(private val module: AndroidDependencyModule) : Application() {

    lateinit var injector: FullMoonInjector

    override fun onCreate() {
        super.onCreate()
        module.context = this
        injector = FullMoonInjector(AppContainer(module))
    }
}
