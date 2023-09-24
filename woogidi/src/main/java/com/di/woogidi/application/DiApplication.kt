package com.di.woogidi.application

import android.app.Application
import com.di.woogidi.AndroidDiInjector

open class DiApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        injector = AndroidDiInjector(
            applicationInstanceContainer = ApplicationInstanceContainer(),
        )
    }

    override fun onTerminate() {
        injector.applicationInstanceContainer.clear()

        super.onTerminate()
    }

    companion object {

        lateinit var injector: AndroidDiInjector
    }
}
