package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository

object RepositoryModule {
    val cartRepository by lazy { DefaultCartRepository() }
    val productRepository by lazy { DefaultProductRepository() }
}
