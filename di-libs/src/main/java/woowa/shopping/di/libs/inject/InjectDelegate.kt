package woowa.shopping.di.libs.inject

import woowa.shopping.di.libs.annotation.InternalApi
import woowa.shopping.di.libs.container.Containers
import woowa.shopping.di.libs.factory.Lifecycle
import woowa.shopping.di.libs.qualify.Qualifier

// TODO : 추후 Scope 기반으로 변경 (ComponentCallbacks, ComponentActivity, Fragment, ViewModel)
inline fun <reified T : Any> inject(
    qualifier: Qualifier? = null,
    lifecycle: Lifecycle = Lifecycle.SINGLETON,
): Lazy<T> {
    return lazy {
        @OptIn(InternalApi::class)
        Containers.resolve(T::class, qualifier, lifecycle)
    }
}
