package com.example.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.di.annotation.LifeCycleScope

abstract class DIActivity : AppCompatActivity() {
    abstract val module: DIModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("DIActivity.onCreate()")
        injectDependencies()
    }

    override fun onDestroy() {
        super.onDestroy()

        println("DIActivity.onDestroy()")
        releaseDependencies()
    }

    private fun injectDependencies() {
        DIInjector.injectModule(module, LifeCycleScope.ACTIVITY)

        val targetActivity = this::class.java.cast(this)
        DIInjector.injectFields(targetActivity)
    }

    private fun releaseDependencies() {
        DIInjector.releaseModule(module::class, LifeCycleScope.ACTIVITY)
    }
}
