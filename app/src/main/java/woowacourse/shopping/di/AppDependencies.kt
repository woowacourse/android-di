package woowacourse.shopping.di

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository

interface AppDependencies {
    val roomCartRepository: CartRepository
    val productRepository: ProductRepository
    val cartDao: CartProductDao
    val inMemoryCartRepository: CartRepository
}
