package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.KClass

object AppContainer {
    private val productRepository: ProductRepository by lazy { ProductRepositoryImpl() }
    private val cartRepository: CartRepository by lazy { CartRepositoryImpl() }

    val repositories: Map<KClass<*>, Any> =
        mapOf(
            ProductRepository::class to productRepository,
            CartRepository::class to cartRepository,
        )
}
