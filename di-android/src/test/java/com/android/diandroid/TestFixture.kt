package com.android.diandroid

import android.os.Bundle

class TestApplication : ApplicationInjector()

class TestActivity : ActivityInjector() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
