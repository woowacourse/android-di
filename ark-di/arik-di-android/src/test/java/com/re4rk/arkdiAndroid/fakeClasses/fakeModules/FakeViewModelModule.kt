package com.re4rk.arkdiAndroid.fakeClasses.fakeModules

import com.re4rk.arkdi.ArkModule
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeViewModelDependency
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeViewModelDependencyImpl

class FakeViewModelModule(parentModule: ArkModule) : ArkModule(parentModule) {
    fun fakeViewModelDependency(): FakeViewModelDependency = FakeViewModelDependencyImpl()
}
