package woowacourse.shopping.fake

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class FakeCartRepository : CartRepository {
    val carts: MutableList<CartProductEntity> = CARTS_ENTITY.toMutableList()

    override suspend fun addCartProduct(product: Product) {
        carts.add(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = carts.map(CartProductEntity::toDomain)

    override suspend fun deleteCartProduct(id: Long) {
        carts.filter { it.id != id }
    }
}

val CARTS_ENTITY =
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

val CART_PRODUCTS =
    CARTS_ENTITY.map(CartProductEntity::toDomain)
