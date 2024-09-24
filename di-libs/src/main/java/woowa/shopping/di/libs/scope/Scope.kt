package woowa.shopping.di.libs.scope

import woowa.shopping.di.libs.annotation.InternalApi
import woowa.shopping.di.libs.container.Containers
import woowa.shopping.di.libs.factory.Lifecycle
import woowa.shopping.di.libs.qualify.Qualifier


data class Scope(
    val scopeQualifier: Qualifier? = null,
    val lifecycle: Lifecycle,
) {
    @OptIn(InternalApi::class)
    inline fun <reified T : Any> get(qualifier: Qualifier? = null): T {
        if (lifecycle == Lifecycle.SCOPED) {
            requireNotNull(scopeQualifier) {
                "Scope에서만 사용 가능한 qualifier입니다."
            }
            return Containers.resolveScopedInstance(scopeQualifier, T::class, qualifier)
        }
        return Containers.resolve(T::class, qualifier, lifecycle)
    }

    inline fun <reified T : Any> inject(
        qualifier: Qualifier? = null,
        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
    ): Lazy<T> = lazy(mode) { get<T>(qualifier) }

    @OptIn(InternalApi::class)
    fun cancel() {
        require(lifecycle == Lifecycle.SCOPED) {
            "Scoped 인 경우에만 Scope를 취소할 수 있습니다."
        }
        requireNotNull(scopeQualifier) {
            "scopeQualifier 는 null 일 수 없습니다."
        }
        Containers.scopeInstanceRegistry.clearScope(scopeQualifier)
    }

    @OptIn(InternalApi::class)
    fun isLocked(): Boolean {
        require(lifecycle == Lifecycle.SCOPED) {
            "Scoped 인 경우에만 Scope를 취소할 수 있습니다."
        }
        requireNotNull(scopeQualifier) {
            "scopeQualifier 는 null 일 수 없습니다."
        }
        return Containers.scopeInstanceRegistry.isLocked(scopeQualifier)
    }
}

@OptIn(InternalApi::class)
fun startScope(qualifier: Qualifier): Scope {
    Containers.scopeInstanceRegistry.unlockScope(qualifier)
    return Scope(qualifier, Lifecycle.SCOPED)
}

@OptIn(InternalApi::class)
fun findScope(qualifier: Qualifier): Scope {
    return Containers.scopeInstanceRegistry.findScope(qualifier)
}

@OptIn(InternalApi::class)
fun isLocked(qualifier: Qualifier): Boolean {
    return Containers.scopeInstanceRegistry.isLocked(qualifier)
}