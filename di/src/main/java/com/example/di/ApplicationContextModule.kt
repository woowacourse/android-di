package com.example.di

import android.app.Application
import android.content.Context
import com.example.di.annotation.InstanceProvideModule
import com.example.di.annotation.Provides

@InstanceProvideModule
object ApplicationContextModule {
    private lateinit var application: Application

    fun initApplication(app: Application) {
        application = app
    }

    @Provides
    fun provideApplicationContext(): Context = application.applicationContext
}
