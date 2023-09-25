package com.ssu.androidi.application

import android.app.Application
import com.ssu.androidi.container.DefaultContainer
import com.ssu.di.injector.Injector
import com.ssu.di.module.Module
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

open class DiApplication(private val moduleClass: KClass<*>) : Application() {
    val injector: Injector = Injector(DefaultContainer())

    override fun onCreate() {
        super.onCreate()

        addModuleInstances()
    }

    private fun addModuleInstances() {
        val module = moduleClass.primaryConstructor!!.call(this) as Module
        injector.addModuleInstances(module)
    }
}
