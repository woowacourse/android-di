package com.m6z1.moongdi

import kotlin.reflect.KClass

fun startMoong(vararg items: Any) {
    items.forEach { item ->
        when (item) {
            is KClass<*> -> {
                val instance = item.instantiate()
                DependencyContainer.register(instance)
            }

            else -> {
                DependencyContainer.register(item)
            }
        }
    }
}
