package com.re4rk.arkdiAndroid.fakeClasses.fakeModules

import com.re4rk.arkdi.ArkModule
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeApplicationDependency
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeApplicationDependencyImpl

class FakeServiceModule(parentModule: ArkModule) : ArkModule(parentModule) {
    fun fakeApplicationDependency(): FakeApplicationDependency = FakeApplicationDependencyImpl()
}
