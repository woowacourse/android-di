package com.woowacourse.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.woowacourse.di.lifecycle.LifecycleTracker

abstract class DiActivity : AppCompatActivity() {
    abstract val module: Module

    private lateinit var lifecycleTracker: LifecycleTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLifecycle()
    }

    private fun setupLifecycle() {
        lifecycleTracker = LifecycleTracker(module)
        lifecycle.addObserver(lifecycleTracker)
    }
}
