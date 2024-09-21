package com.example.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation

class DiContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()
    private val modules = mutableListOf<Any>()

    fun addInstance(
        classType: KClass<*>,
        instance: Any,
    ) {
        instances[classType] = instance
    }

    fun addModule(vararg newModules: Any) {
        modules.addAll(newModules)
    }

    fun getInstanceOrNull(instanceType: KClass<*>): Any? {
        val targetModuleAndFunction = findTargetModule(instanceType)
        if (targetModuleAndFunction.isEmpty()) return null
        val targetFunction = targetModuleAndFunction.values.first()
        val targetModule = targetModuleAndFunction.keys.first()
        val params = targetFunction.parameters.resolve(targetModule)
        return targetFunction.callBy(params)
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
                }.toMap()
        return targetModuleAndFunction
    }

    private fun KClass<*>.getAnnotatedFunctions() =
        functions.filter { function ->
            function.hasAnnotation<Provides>()
        }

    private fun KFunction<*>.isReturnTypeMatchedWith(instanceType: KClass<*>) = returnType.classifier == instanceType

    private fun List<KParameter>.resolve(targetModule: Any): Map<KParameter, Any?> {
        return associateWith { param ->
            if (param.kind == KParameter.Kind.INSTANCE) {
                targetModule
            } else {
                getInstanceOrNull(param.type.classifier as KClass<*>)
            }
        }
    }
}
