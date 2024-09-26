package com.android.diandroid

import android.os.Bundle
import com.android.di.annotation.Qualifier

class TestApplication : ApplicationInjector()

class TestActivity : ActivityInjector() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

class TestService

class OtherService(val testService: TestService)

@Qualifier
annotation class QualifierTest
