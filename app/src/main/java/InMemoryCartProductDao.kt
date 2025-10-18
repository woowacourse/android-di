package woowacourse.shopping

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity

class InMemoryCartProductDao: CartProductDao {
    private val cartProductEntities: MutableList<CartProductEntity> = mutableListOf(
        CartProductEntity(
            name = "우테코 과자",
            price = 10000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
        ),
        CartProductEntity(
            name = "우테코 과자",
            price = 10000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
        ),
        CartProductEntity(
            name = "우테코 과자",
            price = 10000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
        ),
    )

    override suspend fun getAll(): List<CartProductEntity> = cartProductEntities

    override suspend fun insert(cartProduct: CartProductEntity) {
        cartProductEntities.add(cartProduct)
    }


    override suspend fun delete(id: Long) {
        val cartProductEntity = cartProductEntities.find { it.id == id }
        cartProductEntities.remove(cartProductEntity)
    }
}
