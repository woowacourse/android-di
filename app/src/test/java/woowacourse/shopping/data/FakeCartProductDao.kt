package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.fixture.PRODUCT_1
import woowacourse.shopping.fixture.PRODUCT_2

class FakeCartProductDao : CartProductDao {
    private val products: MutableList<CartProductEntity> =
        mutableListOf(
            PRODUCT_1.toEntity().copy(0),
            PRODUCT_2.toEntity().copy(1),
        )

    override suspend fun getAll(): List<CartProductEntity> = products.toList()

    override suspend fun insert(cartProduct: CartProductEntity) {
        products.add(cartProduct.copy(id = products.size.toLong()))
    }

    override suspend fun delete(id: Long) {
        products.removeIf { product: CartProductEntity -> product.id == id }
    }
}
