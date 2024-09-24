package com.woowacourse.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class DiActivity : AppCompatActivity() {
    abstract val module: Module
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        module.install()
        Injector.injectProperty(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        module.clear()
    }
}
