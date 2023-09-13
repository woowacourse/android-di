package com.woowacourse.bunadi.injector

import com.woowacourse.bunadi.annotation.Inject
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
            val returnType = clazz.supertypes.getOrElse(0) { clazz.starProjectedType }
            val annotation = clazz.annotations.firstOrNull()

            return DependencyKey(returnType, annotation)
        }

        fun createDependencyKey(provider: KFunction<*>): DependencyKey {
            val returnType = provider.returnType
            val annotation = provider.annotations.firstOrNull()

            return DependencyKey(returnType, annotation)
        }

        fun createDependencyKey(parameter: KParameter): DependencyKey {
            val returnType = parameter.type
            val annotation = parameter.annotations.firstOrNull()

            return DependencyKey(returnType, annotation)
        }

        fun createDependencyKey(property: KProperty<*>): DependencyKey {
            val returnType = property.returnType
            val annotation = property.annotations.first { it.annotationClass != Inject::class }

            return DependencyKey(returnType, annotation)
        }
    }
}
