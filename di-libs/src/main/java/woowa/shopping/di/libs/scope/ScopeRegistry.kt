package woowa.shopping.di.libs.scope

import org.jetbrains.annotations.VisibleForTesting
import woowa.shopping.di.libs.annotation.InternalApi
import woowa.shopping.di.libs.factory.ScopedInstanceFactory
import woowa.shopping.di.libs.qualify.Qualifier
import kotlin.reflect.KClass

/**
 * [registerInstanceFactories], [registerInstanceFactory] : ScopeRegistry 에 InstanceFactory 를 등록합니다
 * [unLock] : ScopeRegistry 에서 Lock 된 Scope 를 UnLock 합니다. 이 때부터 해당 Scope 에서 Instance 를 가져올 수 있습니다
 * [clearScope] : ScopeRegistry 에서 해당 Scope 에 등록된 InstanceFactory 내 Instance 를 모두 제거합니다
 * [resolve] : unLock 된 Scope 에서 Instance 를 가져옵니다.
 * */
@InternalApi
class ScopeRegistry {
    private val scopedFactoriesMap =
        mutableMapOf<Qualifier, MutableMap<FactoryKey<*>, ScopedInstanceFactory<*>>>()
    private val lockedScope: MutableSet<Qualifier> = mutableSetOf()

    fun unlockScope(scopeQualifier: Qualifier) {
        require(scopeQualifier in lockedScope) {
            "$scopeQualifier 에 해당하는 Scope 가 이미 Unlocked 상태입니다"
        }
        require(scopedFactoriesMap[scopeQualifier] != null) {
            "$scopeQualifier 에 해당하는 Scope 가 존재하지 않습니다"
        }
        lockedScope.remove(scopeQualifier)
    }

    fun registerScope(scopeQualifier: Qualifier) {
        require(scopeQualifier !in scopedFactoriesMap) {
            "$scopeQualifier 에 해당하는 Scope 가 이미 존재합니다"
        }
        scopedFactoriesMap[scopeQualifier] = mutableMapOf()
    }

    fun registerInstanceFactories(
        scopeQualifier: Qualifier,
        factories: List<ScopedInstanceFactory<*>>
    ) {
        factories.forEach { registerInstanceFactory(scopeQualifier, it) }
    }

    fun <T : Any> registerInstanceFactory(
        scopeQualifier: Qualifier,
        factory: ScopedInstanceFactory<T>
    ) {
        lockedScope.add(scopeQualifier) // Lock
        val factories = scopedFactoriesMap[scopeQualifier]
        if (factories == null) {
            scopedFactoriesMap[scopeQualifier] = mutableMapOf(factory.toKey() to factory)
            return
        }
        val newFactoryKey = factory.toKey()
        require(newFactoryKey !in factories.keys) {
            "$newFactoryKey 에 해당하는 ScopeInstanceFactory 가 이미 존재합니다"
        }
        factories[newFactoryKey] = factory
    }

    @InternalApi
    fun <T : Any> resolve(
        scopeQualifier: Qualifier,
        instanceClazz: KClass<T>,
        instanceQualifier: Qualifier? = null,
    ): T? {
        val factoryKey = FactoryKey(instanceClazz, instanceQualifier)
        val factories = scopedFactoriesMap[scopeQualifier]
        require(factories != null) {
            "$scopeQualifier 에 해당하는 Scope 가 없습니다"
        }
        require(isUnLocked(scopeQualifier)) {
            "$scopeQualifier 에 해당하는 Scope 를 만들어주세요"
        }
        val factory: ScopedInstanceFactory<*>? = factories[factoryKey]
        return factory?.instance() as? T
    }

    private fun isUnLocked(scopeQualifier: Qualifier): Boolean {
        return scopeQualifier !in lockedScope
    }

    @InternalApi
    fun clearScope(scopeQualifier: Qualifier) {
        val factories = scopedFactoriesMap[scopeQualifier]
        requireNotNull(factories) {
            "$scopeQualifier 에 해당하는 Scope 가 존재하지 않습니다"
        }
        factories.forEach { (key, factory) -> factory.clear() }
        lockedScope.add(scopeQualifier)
    }

    @VisibleForTesting
    fun clear() {
        scopedFactoriesMap.clear()
        lockedScope.clear()
    }

    private fun ScopedInstanceFactory<*>.toKey() = FactoryKey(instanceClazz, qualifier)

    private data class FactoryKey<T : Any>(val clazz: KClass<T>, val qualifier: Qualifier? = null)
}