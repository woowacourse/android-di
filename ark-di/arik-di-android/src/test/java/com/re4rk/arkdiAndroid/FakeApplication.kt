package com.re4rk.arkdiAndroid

import com.re4rk.arkdiAndroid.arkModules.arkModules
import com.re4rk.arkdiAndroid.fakeModules.FakeActivityModule
import com.re4rk.arkdiAndroid.fakeModules.FakeApplicationModule
import com.re4rk.arkdiAndroid.fakeModules.FakeRetainedActivityModule
import com.re4rk.arkdiAndroid.fakeModules.FakeServiceModule
import com.re4rk.arkdiAndroid.fakeModules.FakeViewModelModule

private val fakeKArkModules = arkModules {
    applicationModule = ::FakeApplicationModule
    serviceModule = ::FakeServiceModule
    activityModule = ::FakeActivityModule
    retainedActivityModule = ::FakeRetainedActivityModule
    viewModelModule = ::FakeViewModelModule
}

class FakeApplication : ArkApplication(fakeKArkModules) {
    val fakeArkModuleFactory = fakeKArkModules

    override fun onCreate() {
        super.onCreate()
        fakeArkModuleFactory.applicationModule(applicationContext).inject(this)
    }
}
