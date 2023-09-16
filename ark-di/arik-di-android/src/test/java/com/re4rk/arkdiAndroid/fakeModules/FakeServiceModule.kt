package com.re4rk.arkdiAndroid.fakeModules

import com.re4rk.arkdi.ArkModule
import com.re4rk.arkdiAndroid.fakeDependency.FakeApplicationDependency
import com.re4rk.arkdiAndroid.fakeDependency.FakeApplicationDependencyImpl

class FakeServiceModule(parentModule: ArkModule) : ArkModule(parentModule) {
    fun fakeApplicationDependency(): FakeApplicationDependency = FakeApplicationDependencyImpl()
}
