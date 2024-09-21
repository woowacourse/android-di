package com.example.di

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation



class DiContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()
    private val modules = mutableListOf<Any>()

    fun addModule(vararg newModules: Any) {
        modules.addAll(newModules)
    }

    fun addInstance(
        classType: KClass<*>,
        instance: Any,
    ) {
        instances[classType] = instance
    }

    fun getInstanceOrNull(targetInstanceType: KClass<*>): Any? {
        val modules =
            modules.filter {
                it::class.objectInstance != null
            }.associateWith {
                getProvidesAnnotatedFunctions()
            }

        val targetObjectAndFunction =
            modules.entries.flatMap { (module, functions) ->
                functions.filter { targetFunction ->
                    targetFunction.returnType.classifier == targetInstanceType
                }.map { module to it }
            }.toMap()

        if (targetObjectAndFunction.isEmpty()) return null

        val targetFunction = targetObjectAndFunction.values.first()
        val params =
            targetFunction.parameters.associateWith { param ->
                if (param.kind == KParameter.Kind.INSTANCE) {
                    targetObjectAndFunction.keys.first()
                } else {
                    getInstanceOrNull(param.type.classifier as KClass<*>)
                }
            }
        return targetFunction.callBy(params)
    }

    private fun getProvidesAnnotatedFunctions() =
        modules.flatMap { module ->
            module::class.functions.filter { function ->
                function.hasAnnotation<Provides>()
            }
        }
}
