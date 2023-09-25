package com.woosuk.scott_di_android

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.hasAnnotation

object DiContainer {
    private val savedDependencies: MutableList<Dependency> = mutableListOf()

    private val values: List<Dependency>
        get() = savedDependencies.toList()

    fun addDependency(dependency: Dependency) {
        savedDependencies.add(dependency)
    }

    fun destroyDependency(dependency: Dependency) {
        savedDependencies.remove(dependency)
    }

    private fun getDependency(
        kClazz: KClass<*>,
        annotation: Annotation?,
    ): Dependency {
        return values.find { it.typeClass == kClazz && it.qualifierAnnotation == annotation }
            ?: throw IllegalStateException("${kClazz.simpleName}해당하는 의존성을 찾을 수 없습니다")
    }

    fun getDependencyInstance(kClazz: KClass<*>, annotation: Annotation?): Any {
        return getDependency(kClazz, annotation).getInstance()
    }
}

fun KParameter.getQualifierAnnotation() =
    annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }

fun KProperty<*>.getQualifierAnnotation() =
    annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }

fun KFunction<*>.getQualifierAnnotation() =
    annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }
