package com.re4rk.arkdiAndroid.fakeClasses.fakeModules

import android.content.Context
import com.re4rk.arkdi.ArkModule
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeRetainedActivityDependency
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeRetainedActivityDependencyImpl

class FakeRetainedActivityModule(parentModule: ArkModule, context: Context) : ArkModule(parentModule) {
    fun fakeRetainedActivityDependency(): FakeRetainedActivityDependency =
        FakeRetainedActivityDependencyImpl()
}
