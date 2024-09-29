package com.example.seogi.fixture

import com.example.seogi.di.DiApplication
import com.example.seogi.di.DiContainer

class FakeApplication : DiApplication() {
    override fun onCreate() {
        super.onCreate()
        module = FakeModule
        diContainer = DiContainer(FakeModule, this)
    }
}
