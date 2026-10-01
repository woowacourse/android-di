package com.cksckckcks.di

import kotlin.reflect.KClass

open class DiContainer {
    private val instances = mutableMapOf<BindingKey, Any>()
    private val factories = mutableMapOf<BindingKey, () -> Any>()

    fun <T : Any> register(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        factory: () -> T,
    ) {
        factories[BindingKey(type, qualifier)] = factory
    }

    fun getInstance(
        targetClass: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any? {
        val key =
            if (qualifier != null) {
                BindingKey(targetClass, qualifier)
            } else {
                val candidates = (instances.keys + factories.keys).filter { it.type == targetClass }.distinct()
                if (candidates.size > 1) {
                    val names = candidates.map { it.qualifier?.simpleName ?: "unqualified" }.sorted()
                    throw IllegalArgumentException(
                        "[Qualifier 지정 필요] ${targetClass.simpleName}에 여러 구현체가 등록되어 있습니다: $names.",
                    )
                }
                candidates.singleOrNull() ?: return null
            }

        instances[key]?.let { return it }

        val factory = factories[key] ?: return null
        val instance = factory()

        instances[key] = instance

        return instance
    }

    fun saveInstance(
        key: KClass<*>,
        value: Any,
        qualifier: KClass<out Annotation>? = null,
    ) {
        instances[BindingKey(key, qualifier)] = value
    }
}
