package com.woowacourse.di

import android.app.Application

abstract class DiApplication : Application() {
    abstract fun install()

    override fun onCreate() {
        super.onCreate()
        install()
    }
}
