package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.mapper.toProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.repository.CartRepository

class DefaultCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<Product> = dao.getAll().map { it.toProduct() }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}
