package com.example.bbottodi.di

import android.app.Application

open class DiApplication : Application() {
    val container: Container = Container()
}
