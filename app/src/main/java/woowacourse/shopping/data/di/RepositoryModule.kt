package woowacourse.shopping.data.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository

class RepositoryModule : Module {
    lateinit var productRepository: ProductRepository
    lateinit var cartRepository: CartRepository

    fun getProductRepositoryImpl(): ProductRepository {
        if (!this::productRepository.isInitialized) {
            productRepository = DefaultProductRepository()
        }
        return productRepository
    }

    fun getCartRepositoryImpl(): CartRepository {
        if (!this::cartRepository.isInitialized) {
            cartRepository = DefaultCartRepository()
        }
        return cartRepository
    }
}