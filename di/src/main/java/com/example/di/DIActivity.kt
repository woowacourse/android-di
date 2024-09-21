package com.example.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class DIActivity : AppCompatActivity() {
    abstract val module: DIModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectDependencies()
    }

    override fun onDestroy() {
        super.onDestroy()

        releaseDependencies()
    }

    private fun injectDependencies() {
        DIInjector.injectModule(module)

        val targetActivity = this::class.java.cast(this)
        DIInjector.injectFields(targetActivity)
    }

    private fun releaseDependencies() {
        DIInjector.releaseModule(module)
    }
}
