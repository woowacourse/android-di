package com.bandal.fullmoon

import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.valueParameters

class AppContainer(private val module: Module) {
    private val instances: HashMap<String, Any> = HashMap()

    private val functionsForCreate: Map<String, KFunction<*>> =
        module::class.declaredMemberFunctions.associateBy { it.getKey() }

    internal fun getSavedInstance(key: String): Any? {
        return instances[key] ?: createInstance(key)
    }

    private fun createInstance(key: String): Any? {
        val function: KFunction<*> =
            functionsForCreate[key] ?: return null
        return callCreateFunction(function)
    }

    private fun callCreateFunction(function: KFunction<*>): Any {
        val parameterInstances = function.valueParameters.map { parameter ->
            getSavedInstance(parameter.getKey())
        }

        val instance: Any = function.call(module, *parameterInstances.toTypedArray())
            ?: throw DIError.NotFoundCreateFunction(function.getKey())

        if (function.hasAnnotation<Singleton>()) {
            instances[function.getKey()] = instance
        }
        return instance
    }
}
