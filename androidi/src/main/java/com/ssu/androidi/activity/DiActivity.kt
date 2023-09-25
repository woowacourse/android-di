package com.ssu.androidi.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ssu.androidi.application.DiApplication
import com.ssu.androidi.container.DefaultContainer
import com.ssu.di.injector.Injector
import com.ssu.di.module.Module
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

abstract class DiActivity(private val moduleClass: KClass<*>) : AppCompatActivity() {
    lateinit var injector: Injector
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector = Injector(DefaultContainer((application as DiApplication).injector.container))
        addModuleInstances()
        injector.injectOnFields(this::class, this)
    }

    private fun addModuleInstances() {
        val module = moduleClass.primaryConstructor!!.call(this) as Module
        injector.addModuleInstances(module)
    }
}
