package com.example.yennydi.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yennydi.application.DiApplication
import com.example.yennydi.di.DependencyProvider

abstract class DiActivity : AppCompatActivity() {
    val instanceContainer = ActivityInstanceContainer()

    abstract val dependencyProvider: DependencyProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(instanceContainer)
        dependencyProvider.register(instanceContainer)
        injectDependencies()
    }

    private fun injectDependencies() {
        val target = this::class.java.cast(this)
        (application as DiApplication).injector.injectProperty(target, instanceContainer)
    }
}
