package com.bandal.fullmoon

import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.findAnnotations

abstract class AppContainer {
    private val needQualifyInstances: HashMap<String, Any> = HashMap()
    private val noDistinctionInstances: HashMap<KClass<*>, Any> = HashMap()

    fun getSavedInstance(injectType: InjectType, type: KClass<*>) = when (injectType) {
        InjectType.NEED_QUALIFY -> needQualifyInstances[
            type.findAnnotations<Qualifier>()
                .first().key,
        ]

        InjectType.NO_DISTINCTION -> noDistinctionInstances[type]
    }

    fun <T> addInstance(injectType: InjectType, type: KClass<*>, instance: T) {
        when (injectType) {
            InjectType.NEED_QUALIFY -> {
                needQualifyInstances[type.getQualifierKey()] = instance as Any
            }

            InjectType.NO_DISTINCTION -> {
                noDistinctionInstances[type] = instance as Any
            }
        }
    }

    private fun KClass<*>.getQualifierKey(): String {
        return this.findAnnotation<Qualifier>()?.key
            ?: throw DIError.NotFoundQualifierOrInstance(this)
    }
}
