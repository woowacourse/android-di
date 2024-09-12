package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.Product

class CartRepositoryImpl(
    private val dao: CartProductDao
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun allCartProducts(): List<Product> {
        return dao.getAll().map {
            it.toProduct()
        }
    }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }

    private fun CartProductEntity.toProduct(): Product {
        return Product(id, name, price, imageUrl, createdAt)
    }
}

interface CartRepository {
    suspend fun addCartProduct(product: Product)
    suspend fun allCartProducts(): List<Product>
    suspend fun deleteCartProduct(id: Long)
}