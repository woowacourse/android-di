package com.example.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation

class DiContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()
    private val sources = mutableMapOf<KClass<*>, Any>()
    private val modules = mutableListOf<Any>()

    fun addInstance(
        classType: KClass<*>,
        instance: Any,
    ) {
        instances[classType] = instance
    }

    fun getInstanceOrNull(instanceType: KClass<*>): Any? = instances[instanceType]

    fun addModule(vararg newModules: Any) {
        modules.addAll(newModules)
    }

    fun getTargetOrNull(instanceType: KClass<*>): Any? {
        return sources[instanceType] ?: run {
            val targetModuleAndFunction = findTargetModule(instanceType)
            if (targetModuleAndFunction.isEmpty()) {
                null
            } else {
                val newInstance = createInstance(targetModuleAndFunction)
                addSource(instanceType, newInstance)
                newInstance
            }
        }
    }

    private fun findTargetModule(instanceType: KClass<*>): Map<Any, KFunction<*>> {
        val targetModuleAndFunction =
            modules
                .filter { it::class.objectInstance != null }
                .associateWith { it::class.getAnnotatedFunctions() }
                .entries
                .flatMap { (module, functions) ->
                    functions.filter {
                        it.isReturnTypeMatchedWith(instanceType)
                    }.map { module to it }
                }
                .toMap()
        return targetModuleAndFunction
    }

    private fun KClass<*>.getAnnotatedFunctions() =
        functions.filter { function ->
            function.hasAnnotation<Provides>()
        }

    private fun KFunction<*>.isReturnTypeMatchedWith(instanceType: KClass<*>) = returnType.classifier == instanceType

    private fun createInstance(targetModuleAndFunction: Map<Any, KFunction<*>>): Any {
        val targetFunction = targetModuleAndFunction.values.first()
        val targetModule = targetModuleAndFunction.keys.first()
        val params = targetFunction.parameters.resolve(targetModule)
        return targetFunction.callBy(params)!!
    }

    private fun List<KParameter>.resolve(targetModule: Any): Map<KParameter, Any?> {
        return associateWith { param ->
            if (param.kind == KParameter.Kind.INSTANCE) {
                targetModule
            } else {
                getTargetOrNull(param.type.classifier as KClass<*>)
            }
        }
    }

    private fun addSource(
        classType: KClass<*>,
        instance: Any,
    ) {
        sources[classType] = instance
    }
}
