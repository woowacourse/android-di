package com.zzang.di.testfixture

import android.app.Application
import com.zzang.di.DIContainer

class FakeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DIContainer.loadModule(FakeModule())
    }
}
