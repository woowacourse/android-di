package woowacourse.shopping.data.cart

import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class InMemoryCartRepository() : CartRepository {
    private val products: MutableList<CartProduct> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        val entity = product.toEntity()
        val cartProduct = CartProduct(id = entity.id, product = product, entity.createdAt)
        products.add(cartProduct)
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return products
    }

    override suspend fun deleteCartProduct(id: Long) {
        products.removeAt(id.toInt())
    }
}
