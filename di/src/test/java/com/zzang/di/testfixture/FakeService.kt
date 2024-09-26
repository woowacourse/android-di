package com.zzang.di.testfixture

import com.zzang.di.annotation.lifecycle.ActivityComponent

@ActivityComponent
class FakeService {
    fun doSomething() {
        println("do something")
    }
}
