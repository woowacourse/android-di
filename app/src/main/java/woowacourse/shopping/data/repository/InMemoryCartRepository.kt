package woowacourse.shopping.data.repository

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.ui.common.di.qualifier.InMemoryCartRepositoryQualifier

@InMemoryCartRepositoryQualifier
class InMemoryCartRepository : CartRepository {
    private val cartProducts = mutableListOf<CartProductEntity>()
    private var lastId: Long = 0

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product.toEntity().apply { id = ++lastId })
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toDomain()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeIf { it.id == id }
    }
}
