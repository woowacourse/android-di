package com.cksckckcks.di

import kotlin.reflect.KClass

class ScopedContainer internal constructor(
    container: DiContainer,
    val type: ScopeType,
    val id: String,
) : AutoCloseable {
    private var container: DiContainer? = container
    private val instances = mutableMapOf<BindingKey, Any>()

    fun getInstance(
        targetClass: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any? = requireContainer().getInstance(targetClass, qualifier, this)

    fun saveInstance(
        key: KClass<*>,
        value: Any,
        qualifier: KClass<out Annotation>? = null,
    ) {
        requireContainer().saveInstance(BindingKey(key, qualifier), value, this)
    }

    override fun close() {
        container?.closeScope(type, id)
    }

    internal fun keysFor(type: KClass<*>): Set<BindingKey> {
        requireContainer()
        return instances.keys.filter { it.type == type }.toSet()
    }

    internal fun getStoredInstance(key: BindingKey): Any? {
        requireContainer()
        return instances[key]
    }

    internal fun store(
        key: BindingKey,
        instance: Any,
    ) {
        requireContainer()
        instances[key] = instance
    }

    internal fun release() {
        instances.clear()
        container = null
    }

    private fun requireContainer(): DiContainer = checkNotNull(container) { "종료된 스코프입니다: ${type.name}/$id" }
}
