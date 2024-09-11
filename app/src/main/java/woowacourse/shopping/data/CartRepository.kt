package woowacourse.shopping.data


class CartRepository(private val cartProductDao: CartProductDao) {

    // 장바구니에 제품 추가
    suspend fun addCartProduct(product: CartProductEntity) {
        cartProductDao.insert(product) // Dao를 통해 데이터베이스에 삽입
    }

    // 모든 장바구니 제품 조회
    suspend fun getAllCartProducts(): List<CartProductEntity> {
        return cartProductDao.getAll() // Dao를 통해 데이터베이스에서 조회
    }

    suspend fun deleteCartProduct(id: Long) {
        cartProductDao.delete(id) // Dao를 통해 데이터베이스에서 삭제
    }
}
