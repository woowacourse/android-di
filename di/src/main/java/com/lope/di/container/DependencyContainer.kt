package com.lope.di.container

import com.lope.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

object DependencyContainer {
    private val instances: HashMap<Pair<KClass<*>, Annotation?>, Any> = hashMapOf()

    fun setInstance(kClass: KClass<*>, instance: Any) {
        val annotationWithQualifier = instance::class.annotations.find {
            it.annotationClass.hasAnnotation<Qualifier>()
        }
        val key = kClass to annotationWithQualifier
        instances[key] = instance
    }

    fun getInstance(kClass: KClass<*>, annotation: Annotation?): Any? {
        return instances[kClass to annotation]
    }

    fun clear() {
        instances.clear()
    }
}
