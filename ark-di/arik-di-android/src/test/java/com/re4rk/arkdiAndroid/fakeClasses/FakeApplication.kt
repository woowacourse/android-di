package com.re4rk.arkdiAndroid.fakeClasses

import com.re4rk.arkdiAndroid.ArkApplication
import com.re4rk.arkdiAndroid.arkModules.arkModules
import com.re4rk.arkdiAndroid.fakeClasses.fakeModules.FakeActivityModule
import com.re4rk.arkdiAndroid.fakeClasses.fakeModules.FakeApplicationModule
import com.re4rk.arkdiAndroid.fakeClasses.fakeModules.FakeRetainedActivityModule
import com.re4rk.arkdiAndroid.fakeClasses.fakeModules.FakeServiceModule
import com.re4rk.arkdiAndroid.fakeClasses.fakeModules.FakeViewModelModule

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
