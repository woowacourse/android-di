package com.re4rk.arkdiAndroid.fakeModules

import com.re4rk.arkdi.ArkModule
import com.re4rk.arkdiAndroid.fakeDependency.FakeRetainedActivityDependency
import com.re4rk.arkdiAndroid.fakeDependency.FakeRetainedActivityDependencyImpl

class FakeRetainedActivityModule(parentModule: ArkModule) : ArkModule(parentModule) {
    fun fakeRetainedActivityDependency(): FakeRetainedActivityDependency =
        FakeRetainedActivityDependencyImpl()
}
