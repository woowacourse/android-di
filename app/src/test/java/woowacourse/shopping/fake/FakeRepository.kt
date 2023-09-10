package woowacourse.shopping.fake

import woowacourse.shopping.getProducts
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class FakeProductRepository : ProductRepository {
    private val products: List<Product> = getProducts()

    override fun getAllProducts(): List<Product> {
        return products
    }
}

class FakeCartRepository(private val cartProducts: MutableList<CartProduct> = mutableListOf()) :
    CartRepository {
    override suspend fun addCartProduct(product: Product) {
        val cartProduct = CartProduct(
            System.currentTimeMillis(),
            product.name,
            product.price,
            product.imageUrl,
            System.currentTimeMillis(),
        )
        cartProducts.add(cartProduct)
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAt(id.toInt())
    }
}
