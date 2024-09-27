package com.woowacourse.di

import android.app.Application

abstract class DiApplication : Application() {
    abstract val module: Module

    override fun onCreate() {
        super.onCreate()
        module.install()
    }

    override fun onTerminate() {
        super.onTerminate()
        module.clear()
    }
}
