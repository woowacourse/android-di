package woowacourse.shopping.di

import kotlin.reflect.KClass

object Container {
    private val providers = mutableMapOf<DependencyKey, () -> Any>()
    private val dependencies = mutableMapOf<DependencyKey, Any>()

    fun <T : Any> register(
        kClass: KClass<T>,
        qualifier: String? = null,
        provider: () -> T,
    ) {
        val key = DependencyKey(kClass, qualifier)
        providers[key] = provider
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(
        kClass: KClass<T>,
        qualifier: String?,
    ): T {
        val key = DependencyKey(kClass, qualifier)
        val provider =
            providers[key]
                ?: throw IllegalArgumentException("${kClass.simpleName} 의존성이 등록되어 있지 않습니다.")
        val instance = provider()
        dependencies[key] = instance
        return instance as T
    }

    fun <T : Any> canResolve(
        kClass: KClass<T>,
        qualifier: String?,
    ): Boolean {
        val key = DependencyKey(kClass, qualifier)
        return dependencies.containsKey(key) || providers.containsKey(key)
    }
}
