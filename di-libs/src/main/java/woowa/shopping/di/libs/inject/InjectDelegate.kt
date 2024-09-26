package woowa.shopping.di.libs.inject

import woowa.shopping.di.libs.annotation.InternalApi
import woowa.shopping.di.libs.container.Containers
import woowa.shopping.di.libs.factory.Lifecycle
import woowa.shopping.di.libs.qualify.Qualifier
import woowa.shopping.di.libs.qualify.qualifier
import kotlin.reflect.KClass

inline fun <reified T : Any> inject(
    instanceQualifier: Qualifier? = null,
    scopeQualifier: Qualifier? = null,
    lifecycle: Lifecycle = Lifecycle.SINGLETON,
): Lazy<T> {
    @OptIn(InternalApi::class)
    return lazy {
        if (lifecycle == Lifecycle.SCOPED) {
            requireNotNull(scopeQualifier) {
                "Scope에서만 사용 가능한 qualifier입니다."
            }
            Containers.resolveScopedInstance(scopeQualifier, T::class, instanceQualifier)
        } else {
            Containers.resolve(T::class, instanceQualifier, lifecycle)
        }
    }
}

inline fun <reified T : Any> injectScopeComponent(
    scopeClazz: KClass<*>,
    instanceQualifier: Qualifier? = null,
): Lazy<T> {
    return inject(instanceQualifier, qualifier(scopeClazz), Lifecycle.SCOPED)
}
