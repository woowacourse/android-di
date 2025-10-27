package woowacourse.shopping.di

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class Container {
    private val providers = mutableMapOf<DependencyKey, () -> Any>()
    private val singletonInstances = ConcurrentHashMap<DependencyKey, Any>()
    private val scopedContainers = ConcurrentHashMap<String, MutableMap<DependencyKey, Any>>()

    fun <T : Any> registerProvider(
        kClass: KClass<T>,
        qualifier: String? = null,
        provider: () -> T,
    ) {
        val key = DependencyKey(kClass, qualifier)
        providers[key] = provider
    }

    fun <T : Any> get(
        kClass: KClass<T>,
        qualifier: String?,
        scope: Scope,
        scopeName: String,
    ): T {
        val key = DependencyKey(kClass, qualifier)
        return when (scope) {
            Scope.SINGLETON -> getSingletonInstance(key)
            Scope.ACTIVITY, Scope.VIEWMODEL -> getScopedInstance(key, scopeName)
            Scope.FACTORY -> createNewInstance(key)
        }
    }

    fun createScope(scopeName: String) {
        scopedContainers[scopeName] = mutableMapOf()
    }

    fun clearScope(scopeName: String) {
        scopedContainers.remove(scopeName)
    }

    fun <T : Any> canResolve(
        kClass: KClass<T>,
        qualifier: String?,
    ): Boolean {
        val key = DependencyKey(kClass, qualifier)
        return singletonInstances.containsKey(key) || providers.containsKey(key)
    }

    private fun <T : Any> getSingletonInstance(key: DependencyKey): T =
        singletonInstances.computeIfAbsent(key) {
            createNewInstance(key) as Any
        } as T

    private fun <T : Any> getScopedInstance(
        key: DependencyKey,
        scopeName: String,
    ): T {
        val scopeContainer = scopedContainers.computeIfAbsent(scopeName) { ConcurrentHashMap() }
        return scopeContainer.computeIfAbsent(key) { createNewInstance(key) as Any } as T
    }

    private fun <T : Any> createNewInstance(key: DependencyKey): T {
        val provider =
            providers[key]
                ?: throw IllegalArgumentException(ERROR_NO_SUCH_DEPENDENCY.format(key.type.simpleName))
        return provider() as T
    }

    companion object {
        private const val ERROR_NO_SUCH_DEPENDENCY = "%s 의존성이 등록되어 있지 않습니다."
    }
}
