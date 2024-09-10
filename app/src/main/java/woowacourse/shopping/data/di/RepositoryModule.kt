package woowacourse.shopping.data.di

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ProductRepository

object RepositoryModule {
    fun install() {
        provideProductRepository()
        provideCartRepository()
    }

    private fun provideProductRepository() {
        DependencyContainer.addInstance(
            ProductRepository::class,
            DefaultCartRepository()
        )
    }

    private fun provideCartRepository() {
        DependencyContainer.addInstance(
            CartRepository::class,
            DefaultCartRepository()
        )
    }
}
