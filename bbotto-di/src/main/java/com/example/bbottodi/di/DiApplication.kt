package com.example.bbottodi.di

import android.app.Application
import android.content.Context

open class DiApplication : Application() {
    lateinit var module: (Context) -> Module
    lateinit var container: Container

    override fun onCreate() {
        super.onCreate()
        container = Container(null, module(this))
    }
}
