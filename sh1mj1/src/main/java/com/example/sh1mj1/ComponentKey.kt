package com.example.sh1mj1

import kotlin.reflect.KClass

data class ComponentKey private constructor(
    val clazz: KClass<*>,
    val qualifier: Qualifier? = null,
) {
    companion object {
        private val cache = mutableMapOf<Pair<KClass<*>, Qualifier?>, ComponentKey>()

        fun of(
            clazz: KClass<*>,
            qualifier: Qualifier? = null,
        ): ComponentKey {
            val key = clazz to qualifier
            return cache.getOrPut(key) {
                ComponentKey(clazz, qualifier)
            }
        }
    }
}

/*
data class ComponentKey private constructor(
    val clazz: KClass<*>,
    val qualifier: Qualifier? = null
) {
    companion object {
        // 캐시를 위한 맵
        private val cache = mutableMapOf<Pair<KClass<*>, Qualifier?>, ComponentKey>()

        // 인스턴스를 반환하는 함수
        fun getInstance(clazz: KClass<*>, qualifier: Qualifier? = null): ComponentKey {
            val key = clazz to qualifier
            return cache.getOrPut(key) {
                ComponentKey(clazz, qualifier)
            }
        }
    }
}

 */
