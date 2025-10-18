package woowacourse.shopping.data

import com.example.di.Remote
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.Product

@Remote
class DefaultCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProductEntity> {
        return dao.getAll()
    }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}
