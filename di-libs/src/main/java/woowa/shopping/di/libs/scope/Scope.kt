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
        return Containers.resolve(T::class, qualifier, lifecycle)
    }

    inline fun <reified T : Any> inject(
        qualifier: Qualifier? = null,
        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
    ): Lazy<T> = lazy(mode) { get<T>(qualifier) }
}
