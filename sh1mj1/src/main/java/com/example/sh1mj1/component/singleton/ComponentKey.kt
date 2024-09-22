package com.example.sh1mj1.component.singleton

import com.example.sh1mj1.annotation.Qualifier
import kotlin.reflect.KClass

@Suppress("DataClassPrivateConstructor")
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
