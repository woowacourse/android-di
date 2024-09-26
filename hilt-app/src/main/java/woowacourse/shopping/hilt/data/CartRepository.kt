package woowacourse.shopping.hilt.data

import woowacourse.shopping.hilt.data.mapper.toEntity
import woowacourse.shopping.hilt.model.Product

class DefaultCartRepository(
    private val dao: CartProductDao,
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

class InMemoryCartRepository : CartRepository {
    private val products = mutableListOf<Product>()

    override suspend fun addCartProduct(product: Product) {
        products.add(product)
    }

    override suspend fun allCartProducts(): List<Product> {
        return products
    }

    override suspend fun deleteCartProduct(id: Long) {
        products.removeIf { it.id == id }
    }
}

interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun allCartProducts(): List<Product>

    suspend fun deleteCartProduct(id: Long)
}
