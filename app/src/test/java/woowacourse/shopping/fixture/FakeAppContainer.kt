package woowacourse.shopping.fixture

import woowacourse.shopping.di.AppContainer
import kotlin.reflect.KClass

class FakeAppContainer : AppContainer {
    private val providers: MutableMap<KClass<*>, Any> = mutableMapOf()

    override fun <T : Any> register(
        kClass: KClass<T>,
        instance: T,
    ) {
        providers[kClass] = instance
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(kClass: KClass<T>): T = providers[kClass] as T

    override fun <T : Any> canResolve(clazz: KClass<T>): Boolean = providers.containsKey(clazz)
}
