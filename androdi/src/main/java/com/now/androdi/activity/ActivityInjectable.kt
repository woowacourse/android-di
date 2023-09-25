package com.now.androdi.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.now.androdi.application.ApplicationInjectable
import com.now.di.Container
import com.now.di.Injector
import com.now.di.Module

abstract class ActivityInjectable : AppCompatActivity() {
    lateinit var parent: ApplicationInjectable
    lateinit var injector: Injector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parent = application as ApplicationInjectable
        injector = parent.activityInjectorManager.getInjector(this::class.java.name)
            ?: Injector(Container(parent.injector.getCurrentContainer()))
    }

    fun injectModule(module: Module) {
        injector.addModule(module)
    }

    override fun onStop() {
        super.onStop()
        when {
            isChangingConfigurations -> {
                parent.activityInjectorManager.saveInjector(this::class.java.name, injector)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        when {
            isFinishing -> {
                parent.activityInjectorManager.removeInjector(this::class.java.name)
            }
        }
    }
}
