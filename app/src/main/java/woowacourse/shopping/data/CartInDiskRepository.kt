package woowacourse.shopping.data

import com.re4rk.arkdi.ArkInject
import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class CartInDiskRepository @ArkInject constructor(
    private val cartProductDao: CartProductDao,
) : CartRepository {

    override suspend fun addCartProduct(product: Product) {
        cartProductDao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProductDao.getAll().map { it.toCartProduct() }
    }

    override suspend fun deleteCartProduct(id: Int) {
        cartProductDao.delete(id.toLong())
    }
}
