package com.example.seogi.di

import android.app.Application

abstract class DiApplication : Application() {
    lateinit var diContainer: DiContainer
}
