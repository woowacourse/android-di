package com.example.bbottodi.di

import android.app.Application

abstract class DiApplication : Application() {
    abstract val container: Container
}
