package woowacourse.shopping.data.repository

import woowacourse.shopping.data.localStorage.CartProductDao
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class CartRepositoryImpl(private val dao: CartProductDao) : CartRepository {

    private val cartProducts: MutableList<Product> = mutableListOf()
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
