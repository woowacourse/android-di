package com.example.seogi.di

import android.app.Application

abstract class DiApplication(private val module: Module) : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
