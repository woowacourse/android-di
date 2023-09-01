package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository

object Singleton {
    val cartRepository = DefaultCartRepository()
    val productRepository = DefaultProductRepository()
}
