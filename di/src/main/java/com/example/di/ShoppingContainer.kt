package com.example.di

import kotlin.reflect.KClass

class ShoppingContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()
    private val makers = mutableMapOf<KClass<*>, () -> Any>()

    fun <T : Any> register(
        type: KClass<T>,
        creator: () -> T,
    ) {
        makers[type] = creator
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(type: KClass<T>): T {
        instances[type]?.let { return it as T }

        val maker: () -> Any = makers[type] ?: error("${type.simpleName} 만드는 방법이 없습니다.")
        val newInstance: Any = maker()
        instances[type] = newInstance
        return newInstance as T
    }
}
