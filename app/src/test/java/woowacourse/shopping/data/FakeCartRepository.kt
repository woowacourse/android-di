package woowacourse.shopping.data

import woowacourse.shopping.model.Product

class FakeCartRepository : CartRepository {
    private var products: List<Product> = emptyList()

    override fun addCartProduct(product: Product) {
        products = products + product
    }

    override fun getAllCartProducts(): List<Product> = products

    override fun deleteCartProduct(id: Int) {
        products = products.filterIndexed { index, _ -> index != id }
    }
}
