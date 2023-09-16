package com.di.berdi

import android.app.Application

abstract class DIApplication : Application() {

    lateinit var injector: Injector

    override fun onCreate() {
        super.onCreate()
        inject()
    }

    abstract fun inject()
}
