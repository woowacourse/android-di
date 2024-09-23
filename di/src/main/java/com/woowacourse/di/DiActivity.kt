package com.woowacourse.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class DiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupModule()
    }

    private fun setupModule() {
        Injector.injectProperty(this)
    }
}
