package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

abstract class RepositoryDIModule : DIModule {
    abstract fun bindCartRepository(defaultCartRepository: DefaultCartRepository): CartRepository

    abstract fun bindProductRepository(defaultProductRepository: DefaultProductRepository): ProductRepository
}
