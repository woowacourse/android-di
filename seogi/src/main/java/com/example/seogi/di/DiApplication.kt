package com.example.seogi.di

import android.app.Application

open class DiApplication : Application() {
    companion object {
        lateinit var diContainer: DiContainer
        lateinit var module: Module
    }
}
