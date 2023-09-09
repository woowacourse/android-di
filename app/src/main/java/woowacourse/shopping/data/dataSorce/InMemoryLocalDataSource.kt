package woowacourse.shopping.data.dataSorce

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class InMemoryLocalDataSource : LocalDataSource {
    private val imMemoryCarts = mutableListOf<CartProduct>()

    override suspend fun getAll(): List<CartProduct> {
        return imMemoryCarts.toList()
    }

    override suspend fun insert(product: Product) {
        imMemoryCarts.add(
            CartProduct(
                imMemoryCarts.size.toLong(),
                System.currentTimeMillis(),
                product,
            ),
        )
    }

    override suspend fun delete(id: Long) {
        imMemoryCarts.removeIf { it.id == id }
    }
}
