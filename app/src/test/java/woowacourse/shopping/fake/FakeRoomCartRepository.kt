package woowacourse.shopping.fake

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class FakeRoomCartRepository : CartRepository {
    override suspend fun addCartProduct(product: Product) = Unit

    override suspend fun getAllCartProducts(): List<CartProduct> = cartProducts

    override suspend fun deleteCartProduct(id: Long) = Unit

    private val cartProductEntities =
        listOf(
            CartProductEntity(
                name = "떡뻥",
                price = 2000,
                imageUrl = "",
            ),
            CartProductEntity(
                name = "떡뻥",
                price = 2000,
                imageUrl = "",
            ),
        )

    private val cartProducts =
        cartProductEntities.map(CartProductEntity::toDomain)
}
