package woowacourse.shopping.data

import com.example.di.Inject
import woowacourse.shopping.data.annotation.InMemoryCartProductDao
import woowacourse.shopping.data.mapper.toCartEntity
import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class DefaultCartRepository @Inject constructor(
    @InMemoryCartProductDao private val cartProductDao: CartProductDao,
) :
    CartRepository {
    override suspend fun addCartProduct(product: Product) {
        cartProductDao.insert(product.toCartEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProductDao.getAll().map { it.toCartProduct() }
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProductDao.delete(id)
    }
}
