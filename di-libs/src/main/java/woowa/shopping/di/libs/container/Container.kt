package woowa.shopping.di.libs.container

import woowa.shopping.di.libs.scope.LifeCycleScope
import woowa.shopping.di.libs.scope.PrototypeScope
import woowa.shopping.di.libs.scope.SingletonScope
import kotlin.reflect.KClass

class Container {
    val cached = mutableMapOf<KClass<*>, LifeCycleScope<*>>()

    fun <T : Any> get(clazz: KClass<T>): T {
        val scope = cached[clazz] ?: error("Dependency not found")
        return scope.instance() as T
    }

    inline fun <reified T : Any> get(): T {
        return get(T::class)
    }

    inline fun <reified T : Any> single(noinline factory: () -> T) {
        put(SingletonScope(factory))
    }

    inline fun <reified T : Any> proto(noinline factory: () -> T) {
        put(PrototypeScope(factory))
    }

    inline fun <reified T : Any> put(scope: LifeCycleScope<T>) {
        cached[T::class] = scope
    }
}

inline fun container(block: Container.() -> Unit) {
    val container = Container()
    container.block()
    Containers.add(container)
}