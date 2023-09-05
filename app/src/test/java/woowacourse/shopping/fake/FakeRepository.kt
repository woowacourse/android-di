package woowacourse.shopping.fake

import woowacourse.shopping.getProducts
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class FakeProductRepository : ProductRepository {
    private val products: List<Product> = getProducts()

    override fun getAllProducts(): List<Product> {
        return products
    }
}

class FakeCartRepository(private val cartProducts: MutableList<Product> = mutableListOf()) :
    CartRepository {
    override fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override fun getAllCartProducts(): List<Product> {
        return cartProducts.toList()
    }

    override fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}
