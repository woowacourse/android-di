package woowacourse.shopping.fixture

import woowacourse.shopping.di.AppContainer
import kotlin.reflect.KClass

class FakeAppContainer : AppContainer {
    private val dependencies: MutableMap<KClass<*>, Any> = mutableMapOf()

    override fun <T : Any> register(
        kClass: KClass<T>,
        instance: T,
        qualifier: String?,
    ) {
        dependencies[kClass] = instance
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(
        kClass: KClass<T>,
        qualifier: String?,
    ): T = dependencies[kClass] as T

    override fun <T : Any> canResolve(
        klass: KClass<T>,
        qualifier: String?,
    ): Boolean = dependencies.containsKey(klass)
}
