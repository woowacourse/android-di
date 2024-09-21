package woowacourse.shopping.data

import com.example.seogi.di.annotation.FieldInject
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.repository.CartRepository

class CartRepositoryOnDisk : CartRepository {
    @FieldInject
    lateinit var dao: CartProductDao

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
