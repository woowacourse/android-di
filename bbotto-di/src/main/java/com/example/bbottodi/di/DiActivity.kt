package com.example.bbottodi.di

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bbottodi.di.inject.AutoDependencyInjector

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
        AutoDependencyInjector.injectField(container, this)
    }
}
