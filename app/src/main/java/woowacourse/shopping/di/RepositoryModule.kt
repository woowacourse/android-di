package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

object RepositoryModule {

    fun init() {
        bindProductRepository()
        bindCartRepository()
    }

    private fun bindProductRepository() {
        DiSingletonComponent.bind(
            bindClassType = ProductRepository::class.java,
            bindClass = ProductRepositoryImpl(),
        )
    }

    private fun bindCartRepository() {
        DiSingletonComponent.bind(
            bindClassType = CartRepository::class.java,
            bindClass = CartRepositoryImpl(),
        )
    }
}
