package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

// TODO: Step2 - CartProductDao를 참조하도록 변경
class DataBaseCartRepository(
    private val cartProductDao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        cartProductDao.insert(product.toEntity())
    }


    override suspend fun deleteCartProduct(id: Long) {
        cartProductDao.delete(id)
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProductDao.getAll().map { it.toCartProduct() }
    }
}
