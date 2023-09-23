package com.now.androdi.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.now.androdi.application.ApplicationInjectable
import com.now.di.Container
import com.now.di.Injector
import com.now.di.Module

abstract class ActivityInjectable : AppCompatActivity() {
    lateinit var injector: Injector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val parent = application as ApplicationInjectable
        injector = Injector(Container(parent.injector.getCurrentContainer()))
    }

    fun injectModule(module: Module) {
        injector.addModule(module)
    }
}
