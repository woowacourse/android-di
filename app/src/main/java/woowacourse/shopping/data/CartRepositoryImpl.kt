package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.mapper.toModel
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class CartRepositoryImpl(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = dao.getAll().map { it.toModel() }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}
