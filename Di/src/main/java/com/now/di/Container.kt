package com.now.di

import kotlin.reflect.KClass

object Container {
    private val dependency = mutableMapOf<DependencyType, Any>()

    fun getInstance(dependencyType: DependencyType): Any? {
        return dependency[dependencyType]
    }

    fun addInstance(type: KClass<*>, instance: Any, annotation: Annotation?) {
        dependency[DependencyType(type, annotation)] = instance
    }

    fun clear() {
        dependency.clear()
    }
}
