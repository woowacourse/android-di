package woowa.shopping.di.libs.scope

import woowa.shopping.di.libs.annotation.InternalApi
import woowa.shopping.di.libs.container.Containers
import woowa.shopping.di.libs.factory.ScopedInstanceFactory
import woowa.shopping.di.libs.qualify.Qualifier

class ScopeDSL(val scope: Scope, val scopeQualifier: Qualifier) {

    @OptIn(InternalApi::class)
    inline fun <reified T : Any> scoped(
        qualifier: Qualifier? = null,
        noinline factory: Scope.() -> T,
    ) {
        Containers.scopeInstanceRegistry.registerInstanceFactory(
            scopeQualifier,
            ScopedInstanceFactory<T>(
                qualifier, T::class, factory = {
                    scope.factory()
                }
            )
        )
    }
}
