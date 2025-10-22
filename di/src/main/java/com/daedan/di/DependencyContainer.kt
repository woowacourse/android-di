package com.daedan.di

import com.daedan.di.qualifier.Qualifier

class DependencyContainer {
    private val container: MutableMap<Qualifier, Any> = mutableMapOf()

    operator fun get(qualifier: Qualifier): Any? = container[qualifier]

    operator fun set(
        qualifier: Qualifier,
        instance: Any,
    ) {
        container[qualifier] = instance
    }

    fun containsKey(qualifier: Qualifier): Boolean = container.containsKey(qualifier)
}
