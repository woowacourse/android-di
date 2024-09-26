package com.example.di.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.di.Injector
import com.example.di.LifecycleTracker

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
        lifecycleTracker = LifecycleTracker(this)
        lifecycle.addObserver(lifecycleTracker)
    }
}
