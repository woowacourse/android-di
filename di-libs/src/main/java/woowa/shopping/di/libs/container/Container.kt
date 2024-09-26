package woowa.shopping.di.libs.container

import woowa.shopping.di.libs.annotation.InternalApi
import woowa.shopping.di.libs.factory.InstanceFactory
import woowa.shopping.di.libs.factory.Lifecycle
import woowa.shopping.di.libs.factory.PrototypeInstanceFactory
import woowa.shopping.di.libs.factory.SingletonInstanceFactory
import woowa.shopping.di.libs.qualify.Qualifier
import woowa.shopping.di.libs.qualify.qualifier
import woowa.shopping.di.libs.scope.Scope
import woowa.shopping.di.libs.scope.ScopeComponent
import woowa.shopping.di.libs.scope.ScopeDSL
import kotlin.reflect.KClass

class Container {
    @InternalApi
    val instanceRegistry = mutableMapOf<Key, InstanceFactory<*>>()

    inline fun <reified T : Any> single(
        qualifier: Qualifier? = null,
        noinline factory: Scope.() -> T,
    ) {
        val scope = Scope(qualifier, lifecycle = Lifecycle.SINGLETON)
        @OptIn(InternalApi::class)
        instanceRegistry[Key(T::class, qualifier, Lifecycle.SINGLETON)] =
            SingletonInstanceFactory(
                qualifier,
                factory = { scope.factory() },
            )
    }

    @OptIn(InternalApi::class)
    inline fun <reified T : Any> proto(
        qualifier: Qualifier? = null,
        noinline factory: Scope.() -> T,
    ) {
        val scope = Scope(qualifier, lifecycle = Lifecycle.PROTOTYPE)
        instanceRegistry[Key(T::class, qualifier, Lifecycle.PROTOTYPE)] =
            PrototypeInstanceFactory(
                qualifier,
                factory = { scope.factory() },
            )
    }

    @OptIn(InternalApi::class)
    fun scope(
        qualifier: Qualifier,
        onRegister: ScopeDSL.() -> Unit,
    ) {
        val scope = Scope(scopeQualifier = qualifier, lifecycle = Lifecycle.SCOPED)
        val scopeDSL = ScopeDSL(scope, qualifier)
        scopeDSL.onRegister()
    }

    inline fun <reified T> scope(noinline configureScope: ScopeDSL.() -> Unit) where T : Any, T : ScopeComponent {
        val qualifier = qualifier<T>()
        scope(qualifier, configureScope)
    }

    data class Key(
        val clazz: KClass<*>,
        val qualifier: Qualifier? = null,
        val lifecycle: Lifecycle = Lifecycle.SINGLETON,
    )
}
