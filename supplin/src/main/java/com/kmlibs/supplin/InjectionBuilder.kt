package com.kmlibs.supplin

import android.content.Context
import com.kmlibs.supplin.annotations.Module
import com.kmlibs.supplin.model.InjectionData
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

class InjectionBuilder {
    private lateinit var context: Context
    private var modules = listOf<KClass<*>>()

    fun context(context: Context) {
        this.context = context
    }

    fun module(vararg modules: KClass<*>) {
        initializeModules(modules)
    }

    private fun initializeModules(modules: Array<out KClass<*>>) {
        modules.forEach { module ->
            requireModuleAnnotation(module)
            addModule(module)
        }
    }

    private fun requireModuleAnnotation(module: KClass<*>) {
        require(module.hasAnnotation<Module>()) {
            EXCEPTION_MODULE_ANNOTATION_DOES_NOT_EXIST.format(module.simpleName)
        }
    }

    private fun addModule(module: KClass<*>) {
        modules += module
    }

    fun build(): InjectionData = InjectionData(modules, context)

    companion object {
        private const val EXCEPTION_MODULE_ANNOTATION_DOES_NOT_EXIST =
            "Module objects %s should be annotated with @Module."
        private const val EXCEPTION_OBJECT_INSTANCE_DOES_NOT_EXIST =
            "Module object %s should have an instance."
    }
}
