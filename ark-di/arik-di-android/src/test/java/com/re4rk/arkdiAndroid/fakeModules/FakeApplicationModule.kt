package com.re4rk.arkdiAndroid.fakeModules

import com.re4rk.arkdi.ArkModule
import com.re4rk.arkdiAndroid.fakeDependency.FakeServiceDependency
import com.re4rk.arkdiAndroid.fakeDependency.FakeServiceDependencyImpl

class FakeApplicationModule : ArkModule() {
    fun fakeApplicationDependency(): FakeServiceDependency = FakeServiceDependencyImpl()
}
