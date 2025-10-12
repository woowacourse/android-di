package woowacourse.di.auto

import kotlin.reflect.KClass

typealias Provider<T> = () -> T

open class Container {
    private data class Key(
        val type: KClass<*>,
        val qualifier: KClass<out Annotation>?,
    )

    private val providers: MutableMap<Key, Provider<*>> = mutableMapOf()
    private val singletons: MutableMap<Key, Any> = mutableMapOf()

    fun <T : Any> bindSingleton(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        creator: Provider<T>,
    ) {
        val key = Key(type, qualifier)
        providers[key] = {
            singletons.getOrPut(key) { creator() }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val key = Key(type, qualifier)
        val provider =
            providers[key]
                ?: error("No provider for ${type.qualifiedName} with qualifier=${qualifier?.qualifiedName}")
        return provider.invoke() as T
    }

    inline fun <reified T : Any> get(qualifier: KClass<out Annotation>? = null): T = get(T::class, qualifier)
}
