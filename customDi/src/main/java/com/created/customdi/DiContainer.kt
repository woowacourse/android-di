package com.created.customdi

import android.content.Context
import com.created.customdi.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.hasAnnotation

object DiContainer {
    private lateinit var context: Any

    private val _modules: MutableList<Any> = mutableListOf()
    val modules: List<Any> get() = _modules.toList()

    private val _instance: MutableMap<Any, KFunction<*>> = mutableMapOf()
    val instance: Map<Any, KFunction<*>> get() = _instance.toMap()

    private val _singletonInstance: MutableMap<KClass<*>, Any> = mutableMapOf()
    val singletonInstance: Map<KClass<*>, Any> get() = _singletonInstance.toMap()

    private val _qualifiedInstance: MutableMap<Any, KFunction<*>> = mutableMapOf()
    val qualifiedInstance: Map<Any, KFunction<*>> get() = _qualifiedInstance.toMap()

    fun setContext(applicationContext: Context) {
        context = applicationContext
    }

    fun setModule(module: Any) {
        if (modules.contains(module)) return
        _modules.add(module)

        module::class.declaredFunctions.forEach { func ->
            if (func.annotations.any { it.annotationClass.hasAnnotation<Qualifier>() }) {
                val type = func.annotations.filter { it.annotationClass.hasAnnotation<Qualifier>() }
                _qualifiedInstance[type] = func
                return@forEach
            }

            _instance[func.returnType] = func
        }
    }

    fun setSingleton(instance: Any, type: KClass<out Any>) {
        _singletonInstance[type] = instance
    }
}
