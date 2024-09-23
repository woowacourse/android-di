package woowacourse.shopping.data

import shopping.di.Qualifier
import shopping.di.QualifierType

class CartRepository(@Qualifier(QualifierType.ROOM_DB) private val cartProductDao: CartProductDao) {

    suspend fun addCartProduct(product: CartProductEntity) {
        cartProductDao.insert(product)
    }

    suspend fun getAllCartProducts(): List<CartProductEntity> {
        return cartProductDao.getAll()
    }

    suspend fun deleteCartProduct(id: Long) {
        cartProductDao.delete(id)
    }
}
