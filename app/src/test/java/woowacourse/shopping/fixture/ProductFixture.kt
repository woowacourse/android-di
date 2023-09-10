package woowacourse.shopping.fixture

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

object ProductFixture {
    fun getProduct(id: Int = 1) = Product("Product$id", id * 1000, "product$id.com")

    fun getCartProduct(id: Long = 0L) =
        CartProduct(id, "Product$id", id.toInt() * 1000, "product$id.com", 0L)

    fun getProducts(ids: List<Int>) = ids.map { getProduct(it) }

    fun getCartProducts(ids: List<Long>) = ids.map { getCartProduct(it) }
}
