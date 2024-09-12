package woowacourse.shopping.fixture

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.repository.CartRepository

class FakeCartRepositoryInMemory : CartRepository {
    private val cartProducts: MutableList<CartProductEntity> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProductEntity> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAt(id.toInt())
    }

    companion object {
        @Volatile
        private var instance: FakeCartRepositoryInMemory? = null

        fun getInstance(): FakeCartRepositoryInMemory {
            return instance ?: synchronized(this) {
                instance ?: FakeCartRepositoryInMemory().also { instance = it }
            }
        }
    }
}
