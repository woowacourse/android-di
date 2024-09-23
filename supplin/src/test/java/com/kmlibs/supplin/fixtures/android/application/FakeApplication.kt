package com.kmlibs.supplin.fixtures.android.application

import android.app.Application
import com.kmlibs.supplin.Injector
import com.kmlibs.supplin.fixtures.android.module.FakeConcreteModule
import com.kmlibs.supplin.fixtures.android.module.FakeDataSourceModule
import com.kmlibs.supplin.fixtures.android.module.FakeRepositoryModule

class FakeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Injector.init {
            applicationModule(
                this@FakeApplication,
                FakeConcreteModule::class,
                FakeRepositoryModule::class,
                FakeDataSourceModule::class
            )
        }
    }
}
