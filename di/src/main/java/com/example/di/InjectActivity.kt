package com.example.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class InjectActivity : AppCompatActivity() {
    private lateinit var lifecycleTracker: LifecycleTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLifecycle()
        injectFields()
    }

    private fun injectFields() {
        Injector.injectFields(this)
    }

    private fun setupLifecycle() {
        lifecycleTracker = LifecycleTracker()
        lifecycle.addObserver(lifecycleTracker)
    }
}
