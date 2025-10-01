package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.KClass

object AppContainer {
    private val providers = mutableMapOf<KClass<*>, Any>()

    init {
        providers[CartRepository::class] = CartRepositoryImpl()
        providers[ProductRepository::class] = ProductRepositoryImpl()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(clazz: KClass<T>): T =
        providers[clazz] as? T
            ?: throw IllegalArgumentException("${clazz.simpleName} provider 없음")
}
