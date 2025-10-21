package com.example.di

import kotlin.reflect.KClass

class ActivityContainer(
    private val makers: Map<Key, () -> Any>,
) {
    private val scopedInstances = mutableMapOf<Key, Any>()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(
        type: KClass<T>,
        qualifier: String? = null,
    ): T {
        val key = Key(type, qualifier)
        scopedInstances[key]?.let { return it as T }

        val maker: () -> Any =
            makers[key]
                ?: error("${type.simpleName}[qualifier=$qualifier] 만드는 방법이 없습니다.")
        val newInstance: Any = maker()
        scopedInstances[key] = newInstance
        return newInstance as T
    }

    fun clear() {
        scopedInstances.clear()
    }
}
