package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

object RepositoryModule {
    val provideCartRepository: CartRepository by lazy { CartRepositoryImpl() }

    val provideProductRepository: ProductRepository by lazy { ProductRepositoryImpl() }
}
