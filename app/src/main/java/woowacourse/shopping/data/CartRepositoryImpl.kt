package woowacourse.shopping.data

import woowacourse.shopping.application.ShoppingApplication
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class CartRepositoryImpl : CartRepository {

    override suspend fun addCartProduct(product: Product) {
        val db = ShoppingDatabase.getInstance(ShoppingApplication.getApplicationContext())
        db!!.cartProductDao().insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<Product> {
        val db = ShoppingDatabase.getInstance(ShoppingApplication.getApplicationContext())
        return db!!.cartProductDao().getAll().map { it.toDomain() }
    }

    override suspend fun deleteCartProduct(id: Int) {
        val db = ShoppingDatabase.getInstance(ShoppingApplication.getApplicationContext())
        db!!.cartProductDao().delete(id.toLong())
    }
}
