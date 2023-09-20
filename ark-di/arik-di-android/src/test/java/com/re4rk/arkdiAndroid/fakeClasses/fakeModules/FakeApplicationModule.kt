package com.re4rk.arkdiAndroid.fakeClasses.fakeModules

import android.content.Context
import com.re4rk.arkdi.ArkModule
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeApplicationDependency
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeApplicationDependencyImpl

class FakeApplicationModule(context: Context) : ArkModule() {
    fun fakeApplicationDependency(): FakeApplicationDependency = FakeApplicationDependencyImpl()
}
