package com.re4rk.arkdiAndroid.fakeClasses.fakeModules

import android.content.Context
import com.re4rk.arkdi.ArkModule
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeServiceDependency
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeServiceDependencyImpl

class FakeApplicationModule(context: Context) : ArkModule() {
    fun fakeApplicationDependency(): FakeServiceDependency = FakeServiceDependencyImpl()
}
