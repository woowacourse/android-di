package woowacourse.shopping.di

import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

object FakeRepositoryModule : Module {
    fun provideProductRepository(): ProductRepository = ProductFakeRepository()
    fun provideCartRepository(): CartRepository = CartFakeRepository()
}

class ProductFakeRepository() : ProductRepository {
    override fun getAllProducts(): List<Product> {
        TODO("Not yet implemented")
    }
}

class CartFakeRepository() : CartRepository {
    override fun addCartProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override fun getAllCartProducts(): List<Product> {
        TODO("Not yet implemented")
    }

    override fun deleteCartProduct(id: Int) {
        TODO("Not yet implemented")
    }
}
