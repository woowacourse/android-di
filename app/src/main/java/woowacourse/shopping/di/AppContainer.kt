package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultRepository
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.KClass

class AppContainer {
    private val providers = mutableMapOf<KClass<*>, Any>()

    init {
        providers[CartRepository::class] = DefaultCartRepository()
        providers[ProductRepository::class] = DefaultRepository()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(clazz: KClass<T>): T =
        providers[clazz] as? T
            ?: throw IllegalArgumentException("${clazz.simpleName} provider 없음")
}
