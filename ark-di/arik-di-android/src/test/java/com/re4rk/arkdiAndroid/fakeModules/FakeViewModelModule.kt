package com.re4rk.arkdiAndroid.fakeModules

import com.re4rk.arkdi.ArkModule
import com.re4rk.arkdiAndroid.fakeDependency.FakeViewModelDependency
import com.re4rk.arkdiAndroid.fakeDependency.FakeViewModelDependencyImpl

class FakeViewModelModule(parentModule: ArkModule) : ArkModule(parentModule) {
    fun fakeViewModelDependency(): FakeViewModelDependency = FakeViewModelDependencyImpl()
}
