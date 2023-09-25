package com.example.bbottodi.di

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bbottodi.di.inject.AutoDependencyInjector.injectField

open class DiActivity : AppCompatActivity() {
    lateinit var container: Container
    lateinit var module: (Context) -> Module

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        container = ContainerWithContext(
            (application as DiApplication).container,
            module(this),
            this,
        )
        injectField(container, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        container = Container(null, module(this))
    }
}
