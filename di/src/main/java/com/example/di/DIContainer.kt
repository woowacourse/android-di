package com.example.di

import kotlin.reflect.KClass

object DIContainer {
    private val dependencies: MutableList<Dependency> = mutableListOf()

    private val values: List<Dependency>
        get() = dependencies.toList()

    fun addDependency(dependency: Dependency) {
        dependencies.add(dependency)
    }

    fun destroyDependency(dependency: Dependency) {
        dependencies.remove(dependency)
    }

    fun getDependencyInstance(
        kClazz: KClass<*>,
        annotation: Annotation?,
    ): Any = getDependency(kClazz, annotation).getInstance()

    private fun getDependency(
        kClazz: KClass<*>,
        annotation: Annotation?,
    ): Dependency =
        values.find { it.typeClass == kClazz && it.qualifierAnnotation == annotation }
            ?: throw IllegalStateException("${kClazz.simpleName} can not find the corresponding dependency")
}
