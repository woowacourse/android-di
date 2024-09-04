package woowacourse.shopping.data.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.di.component.DiSingletonComponent
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

object RepositoryModule {
    fun install() {
        bindCartRepository()
        bindProductRepository()
    }

    private fun bindCartRepository() {
        DiSingletonComponent.bind(
            CartRepository::class.java,
            CartRepositoryImpl(),
        )
    }

    private fun bindProductRepository() {
        DiSingletonComponent.bind(
            ProductRepository::class.java,
            ProductRepositoryImpl(),
        )
    }
}
