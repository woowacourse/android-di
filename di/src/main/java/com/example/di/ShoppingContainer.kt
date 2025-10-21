package com.example.di

import kotlin.reflect.KClass

private data class Key(
    val type: KClass<*>,
    val qualifier: String?,
)

class ShoppingContainer {
    private val instances = mutableMapOf<Key, Any>()
    private val makers = mutableMapOf<Key, () -> Any>()

    fun <T : Any> register(
        type: KClass<T>,
        qualifier: String? = null,
        creator: () -> T,
    ) {
        val key =
            Key(
                type = type,
                qualifier = qualifier,
            )
        makers[key] = creator
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(
        type: KClass<T>,
        qualifier: String? = null,
    ): T {
        val key = Key(type, qualifier)
        instances[key]?.let { return it as T }

        val maker: () -> Any = makers[key] ?: error("${type.simpleName}[qualifier=$qualifier] 만드는 방법이 없습니다. 등록된 키=${makers.keys}")
        val newInstance: Any = maker()
        instances[key] = newInstance
        return newInstance as T
    }
}
