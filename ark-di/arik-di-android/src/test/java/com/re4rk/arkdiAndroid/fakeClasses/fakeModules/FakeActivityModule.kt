package com.re4rk.arkdiAndroid.fakeClasses.fakeModules

import android.content.Context
import com.re4rk.arkdi.ArkModule
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeActivityDependency
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeActivityDependencyImpl

class FakeActivityModule(parentModule: ArkModule, context: Context) : ArkModule(parentModule) {
    fun fakeActivityDependency(): FakeActivityDependency = FakeActivityDependencyImpl()
}
