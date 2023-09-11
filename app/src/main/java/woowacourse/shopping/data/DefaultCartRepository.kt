package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class DefaultCartRepository(private val db: ShoppingDatabase) : CartRepository {

    override suspend fun addCartProduct(product: Product): Long {
        return db.cartProductDao().insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return db.cartProductDao().getAll().map { it.toDomain() }
    }

    override suspend fun deleteCartProduct(id: Int) {
        db.cartProductDao().delete(id.toLong())
    }
}
