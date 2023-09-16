package com.re4rk.arkdiAndroid.fakeModules

import android.content.Context
import com.re4rk.arkdi.ArkModule
import com.re4rk.arkdiAndroid.fakeDependency.FakeServiceDependency
import com.re4rk.arkdiAndroid.fakeDependency.FakeServiceDependencyImpl

class FakeApplicationModule(context: Context) : ArkModule() {
    fun fakeApplicationDependency(): FakeServiceDependency = FakeServiceDependencyImpl()
}
