package com.bandal.fullmoon

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.jvm.jvmErasure

abstract class AppContainer {
    private val needQualifyInstances: HashMap<String, Any> = HashMap()
    private val noDistinctionInstances: HashMap<KClass<*>, Any> = HashMap()

    fun getSavedInstance(injectType: InjectType, type: KClass<*>): Any? = when (injectType) {
        InjectType.NEED_QUALIFY -> needQualifyInstances[
            type.findAnnotations<Qualifier>()
                .first().key,
        ]

        InjectType.NO_DISTINCTION -> noDistinctionInstances[type]
    }

    fun getSavedInstance(injectType: InjectType, type: KProperty1<*, *>): Any? = when (injectType) {
        InjectType.NEED_QUALIFY -> needQualifyInstances[
            type.findAnnotations<Qualifier>()
                .first().key,
        ]

        InjectType.NO_DISTINCTION -> noDistinctionInstances[type.returnType.jvmErasure]
    }

    fun getSavedInstance(injectType: InjectType, type: KParameter): Any? = when (injectType) {
        InjectType.NEED_QUALIFY -> needQualifyInstances[
            type.findAnnotations<Qualifier>()
                .first().key,
        ]

        InjectType.NO_DISTINCTION -> noDistinctionInstances[type.type.jvmErasure]
    }

    fun <T> addInstance(
        injectType: InjectType,
        type: KClass<*>,
        instance: T,
        qualifierKey: String? = null,
    ) = when (injectType) {
        InjectType.NEED_QUALIFY -> needQualifyInstances[qualifierKey!!] = instance as Any
        InjectType.NO_DISTINCTION -> noDistinctionInstances[type] = instance as Any
    }

    private fun KClass<*>.getQualifierKey(): String {
        return this.findAnnotation<Qualifier>()?.key
            ?: throw DIError.NotFoundQualifierOrInstance(this)
    }
}
