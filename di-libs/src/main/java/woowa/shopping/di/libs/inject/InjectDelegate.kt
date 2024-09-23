package woowa.shopping.di.libs.inject

import woowa.shopping.di.libs.annotation.InternalApi
import woowa.shopping.di.libs.container.Containers
import woowa.shopping.di.libs.factory.Lifecycle
import woowa.shopping.di.libs.qualify.Qualifier

inline fun <reified T : Any> inject(
    qualifier: Qualifier? = null,
    lifecycle: Lifecycle = Lifecycle.SINGLETON,
): Lazy<T> {
    return lazy {
        @OptIn(InternalApi::class)
        Containers.resolve(T::class, qualifier, lifecycle)
    }
}
