package org.library.haeum

import android.content.Context
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.hasAnnotation

object ModuleInjector {
    lateinit var container: Container

    fun initializeModuleInjector(
        context: Context,
        vararg modules: KClass<out Any>,
    ) {
        val instances =
            modules.filter { it.hasAnnotation<Module>() }.map {
                it.objectInstance ?: it.createInstance()
            }
        container = Container(context, instances)
    }
}