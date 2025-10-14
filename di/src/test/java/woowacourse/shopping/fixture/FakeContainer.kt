package woowacourse.shopping.fixture

import woowacourse.shopping.di.Container
import kotlin.reflect.KClass

class FakeContainer : Container {
    private val dependencies: MutableMap<Pair<KClass<*>, String?>, Any> = mutableMapOf()

    override fun <T : Any> register(
        kClass: KClass<T>,
        instance: T,
        qualifier: String?,
    ) {
        dependencies[kClass to qualifier] = instance
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(
        kClass: KClass<T>,
        qualifier: String?,
    ): T = dependencies[kClass to qualifier] as T

    override fun <T : Any> canResolve(
        kClass: KClass<T>,
        qualifier: String?,
    ): Boolean = dependencies.containsKey(kClass to qualifier)
}
