package woowacourse.fixture

import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository

class FakeCartRepository(
    private val fakeAllCartProducts: List<Product>,
) : CartRepository {
    var isProductAdded = false
        private set
    var isProductDeleted = false
        private set

    override fun addCartProduct(product: Product) {
        isProductAdded = true
    }

    override fun getAllCartProducts(): List<Product> = fakeAllCartProducts

    override fun deleteCartProduct(id: Int) {
        isProductDeleted = true
    }
}
