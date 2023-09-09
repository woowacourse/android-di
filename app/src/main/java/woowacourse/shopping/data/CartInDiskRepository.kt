package woowacourse.shopping.data

import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class CartInDiskRepository(
    private val cartProductDao: CartProductDao,
) : CartRepository {

    override fun addCartProduct(product: Product) {
    }

    override fun getAllCartProducts(): List<Product> {
        return emptyList()
    }

    override fun deleteCartProduct(id: Int) {
    }
}
