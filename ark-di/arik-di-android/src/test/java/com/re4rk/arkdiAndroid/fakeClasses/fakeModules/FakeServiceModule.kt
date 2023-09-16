package com.re4rk.arkdiAndroid.fakeClasses.fakeModules

import com.re4rk.arkdi.ArkModule
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeServiceDependency
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeServiceDependencyImpl

class FakeServiceModule(parentModule: ArkModule) : ArkModule(parentModule) {
    fun fakeApplicationDependency(): FakeServiceDependency = FakeServiceDependencyImpl()
}
