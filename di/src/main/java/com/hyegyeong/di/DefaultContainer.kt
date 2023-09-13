package com.hyegyeong.di

import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmErasure

object DefaultContainer : Container {
    override val instances: MutableMap<AnnotationType, Any> = mutableMapOf()

    override fun addInstance(instance: Any) {
        val kclazz = instance::class
        val annotations: List<Annotation> = kclazz.annotations
        val annotationType =
            AnnotationType(annotations.getOrNull(0), kclazz.supertypes[0].jvmErasure)
        instances[annotationType] = instance
    }

    override fun getInstance(annotationType: AnnotationType): Any? {
        return instances[annotationType]
    }

    override fun hasDuplicateObjectsOfType(kClass: KClass<*>): Boolean {
        return instances.any { it.key.clazz == kClass }
    }
}