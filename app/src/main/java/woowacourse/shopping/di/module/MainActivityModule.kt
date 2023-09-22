package woowacourse.shopping.di.module

import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.model.repository.ProductRepository

object MainActivityModule {
    fun provideProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }
}
