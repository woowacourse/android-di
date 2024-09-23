package com.woowacourse.di

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

abstract class DiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupModule()
    }

    private fun setupModule() {
        DependencyContainer.injectProperty(this)
    }
}
