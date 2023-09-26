package com.bandal.fullmoon

import javax.inject.Singleton
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.valueParameters

class AppContainer(private val module: Module) {
    private val instances: HashMap<String, Any> = HashMap()

    private val functionsForCreateInstance: Map<String, KFunction<*>> =
        module::class.declaredMemberFunctions.associateBy { it.getKey() }

    internal fun getSavedInstance(key: String): Any? {
        return instances[key] ?: createInstance(key)
    }

    private fun createInstance(key: String): Any? {
        val function: KFunction<*> = functionsForCreateInstance[key] ?: return null
        return callCreateFunction(function)
    }

    private fun callCreateFunction(function: KFunction<*>): Any {
        val argumentsForInject = function.valueParameters.map { parameter ->
            getSavedInstance(parameter.getKey())
        }

        val createdInstance: Any = function.call(module, *argumentsForInject.toTypedArray())
            ?: throw DIError.NotFoundCreateFunction(function.getKey())

        if (function.hasAnnotation<Singleton>()) {
            instances[function.getKey()] = createdInstance
        }
        return createdInstance
    }
}
