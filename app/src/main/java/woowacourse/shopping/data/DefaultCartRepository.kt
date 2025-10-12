package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.Product

interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<Product>

    suspend fun deleteCartProduct(id: Int)
}

class DefaultCartRepository(
    private val dao: CartProductDao
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<Product> {
        return dao.getAll().map { it.toProduct() }
    }

    override suspend fun deleteCartProduct(id: Int) {
        dao.delete(id)
    }
}
