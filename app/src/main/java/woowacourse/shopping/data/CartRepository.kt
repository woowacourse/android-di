package woowacourse.shopping.data

import woowacourse.shopping.di.Qualifier


class CartRepository(@Qualifier("RoomDB") private val cartProductDao: CartProductDao) {

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
