package woowacourse.shopping.data

import com.kmlibs.supplin.annotations.Supply
import woowacourse.shopping.data.mapper.toCartedProduct
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.di.DatabaseRepository
import woowacourse.shopping.model.CartedProduct
import woowacourse.shopping.model.Product

@DatabaseRepository
class DBCartRepository
    @Supply
    constructor(
        private val cartProductDao: CartProductDao,
    ) : CartRepository {
        override suspend fun addCartProduct(product: Product) {
            cartProductDao.insert(product.toEntity())
        }

        override suspend fun getAllCartProducts(): List<CartedProduct> {
            return cartProductDao.getAll().map(CartProductEntity::toCartedProduct)
        }

        override suspend fun deleteCartProduct(id: Long) {
            cartProductDao.delete(id)
        }
    }
