package com.bandal.halfmoon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bandal.fullmoon.AppContainer
import com.bandal.fullmoon.FullMoonInjector
import com.bandal.fullmoon.Module

abstract class HalfMoonActivity(
    private val module: AndroidDependencyModule = DefaultModule(),
) : AppCompatActivity() {

    private val tag = this::class.simpleName.toString()

    private lateinit var injector: FullMoonInjector

    val halfMoonViewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return injector.inject(modelClass.kotlin)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector = (application as HalfMoonApplication).injector

        setupLifecycleTracker()
        setupActivityContainer()

        injector.injectFields(this::class, this)
    }

    private fun setupActivityContainer() {
        if (!injector.hasContainer(tag)) {
            module.context = this
            val activityContainer = AppContainer(module as Module)
            injector.setContainer(tag, activityContainer)
        }
    }

    private fun setupLifecycleTracker() {
        val activityLifecycleTracker = ActivityLifecycleTracker()
        lifecycle.addObserver(activityLifecycleTracker)
    }
}
