package woowacourse.di

import kotlin.reflect.KClass

object DIContainer {
    private val instances = mutableMapOf<Pair<KClass<*>, KClass<out Annotation>?>, Any>()
    private val providers = mutableMapOf<Pair<KClass<*>, KClass<out Annotation>?>, () -> Any>()

    fun <T : Any> register(
        clazz: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        creator: () -> T,
    ) {
        providers[clazz to qualifier] = creator
    }

    fun <T : Any> get(
        clazz: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val key = clazz to qualifier
        if (instances.containsKey(key)) return instances[key] as T
        val creator = providers[key] ?: throw Exception("No provider for $clazz with qualifier $qualifier")
        val instance = creator()
        instances[key] = instance
        return instance as T
    }
}
