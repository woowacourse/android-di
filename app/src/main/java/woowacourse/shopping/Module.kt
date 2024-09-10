package woowacourse.shopping

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository

object Module {
    fun provideCartRepository(): CartRepository {
        return DefaultCartRepository()
    }

    fun provideProductRepository(): ProductRepository {
        return DefaultProductRepository
    }
}
