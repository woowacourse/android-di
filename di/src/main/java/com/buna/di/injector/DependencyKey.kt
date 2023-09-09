package com.buna.di.injector

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

data class DependencyKey(
    val type: KType,
    val annotation: Annotation? = null,
) {
    companion object {
        fun createDependencyKey(clazz: KClass<*>): DependencyKey {
            val annotation = clazz.annotations.firstOrNull()
            val type = clazz.starProjectedType

            return DependencyKey(type, annotation)
        }

        fun createDependencyKey(provider: KFunction<*>): DependencyKey {
            val returnType = provider.returnType
            val annotation = provider.annotations.firstOrNull()

            return DependencyKey(returnType, annotation)
        }
    }
}
