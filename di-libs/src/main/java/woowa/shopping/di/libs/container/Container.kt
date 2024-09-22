package woowa.shopping.di.libs.container

import woowa.shopping.di.libs.annotation.InternalApi
import woowa.shopping.di.libs.factory.InstanceFactory
import woowa.shopping.di.libs.factory.Lifecycle
import woowa.shopping.di.libs.factory.PrototypeInstanceFactory
import woowa.shopping.di.libs.factory.SingletonInstanceFactory
import woowa.shopping.di.libs.qualify.Qualifier
import woowa.shopping.di.libs.scope.Scope
import kotlin.reflect.KClass

class Container {
    @InternalApi
    val instanceRegistry = mutableMapOf<Key, InstanceFactory<*>>()

    @OptIn(InternalApi::class)
    inline fun <reified T : Any> single(
        qualifier: Qualifier? = null,
        noinline factory: Scope.() -> T,
    ) {
        val scope = Scope(lifecycle = Lifecycle.SINGLETON)
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
        val scope = Scope(lifecycle = Lifecycle.PROTOTYPE)
        instanceRegistry[Key(T::class, qualifier, Lifecycle.PROTOTYPE)] =
            PrototypeInstanceFactory(
                qualifier,
                factory = { scope.factory() },
            )
    }

    data class Key(
        val clazz: KClass<*>,
        val qualifier: Qualifier? = null,
        val lifecycle: Lifecycle = Lifecycle.SINGLETON,
    )
}
