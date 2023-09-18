package com.woowacourse.bunadi.injector

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

data class DependencyKey(
    val type: KType,
    val annotation: Annotation? = null,
) {
    companion object {
        fun createDependencyKey(clazz: KClass<*>): DependencyKey {
            val returnType = clazz.starProjectedType
            return DependencyKey(returnType)
        }

        fun createDependencyKey(provider: KFunction<*>): DependencyKey {
            val returnType = provider.returnType
            return DependencyKey(returnType)
        }

        fun createDependencyKey(parameter: KParameter): DependencyKey {
            val returnType = parameter.type
            return DependencyKey(returnType)
        }

        fun createDependencyKey(property: KProperty<*>): DependencyKey {
            val returnType = property.returnType
            return DependencyKey(returnType)
        }
    }
}
