package woowacourse.di

import kotlin.reflect.KClass

class DependencyScope internal constructor(
    private val parent: DependencyScope?
) {
    private val caches = mutableMapOf<DependencyContainer.DependencyKey, Any>()

    internal fun find(key: DependencyContainer.DependencyKey): Any? =
        caches[key] ?: parent?.find(key)

    internal fun getOrCreate(
        key: DependencyContainer.DependencyKey,
        create: () -> Any,
    ) : Any = caches.getOrPut(key, create)

    internal fun register(
        key: DependencyContainer.DependencyKey,
        instance: Any,
    ) {
        caches[key] = instance
    }

    internal fun hasQualifiedBinding(type: KClass<*>): Boolean =
        caches.keys.any { it.type == type && it.qualifier != null } ||
                parent?.hasQualifiedBinding(type) == true

    internal fun close() {
        caches.clear()
    }
}