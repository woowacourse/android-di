package woowacourse.shopping.fake

import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class FakeCartRepository : CartRepository {
    private val cartProducts = testDatas.map { it.toEntity().toDomain() }
        .toMutableList()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product.toEntity().toDomain())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAt(id.toInt())
    }
}
