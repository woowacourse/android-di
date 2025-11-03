package woowacourse.shopping.di

import kotlin.reflect.KClass

class Container {
    private val dependencies = mutableMapOf<DependencyKey, Any>()
    private val providers = mutableMapOf<DependencyKey, () -> Any>()

    fun <T : Any> registerInstances(
        kClass: KClass<T>,
        instance: T,
        qualifier: String? = null,
    ) {
        dependencies[DependencyKey(kClass, qualifier)] = instance
    }

    fun <T : Any> registerProvider(
        kClass: KClass<T>,
        qualifier: String? = null,
        provider: () -> T,
    ) {
        providers[DependencyKey(kClass, qualifier)] = provider
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(
        kClass: KClass<T>,
        qualifier: String? = null,
    ): T? {
        val key = DependencyKey(kClass, qualifier)

        // 이미 존재하는 인스턴스면 그대로 반환
        val existing = dependencies[key] as? T
        if (existing != null) return existing

        // provider가 등록되어 있다면 새 인스턴스를 생성하고 저장
        val provider = providers[key] ?: return null
        val newInstance = provider.invoke()
        dependencies[key] = newInstance

        return newInstance as T
    }
}
