package woowacourse.shopping.ui.injection

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

interface RepositoryDI

class RepositoryBinder {
    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()

    fun provideCartRepository(): CartRepository = CartRepositoryImpl()
}
