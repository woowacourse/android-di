package woowacourse.shopping

import woowacourse.shopping.di.DependencyRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

class AppContainer : DependencyRepository {
    private val instances = mutableMapOf<KClass<*>, Any>()

    override fun <T : Any> get(kClass: KClass<T>): T? {
        return instances[kClass]?.let { kClass.cast(it) }
    }

    override fun <T : Any> save(kClass: KClass<T>, instance: T) {
        instances[kClass] = instance
    }
}
