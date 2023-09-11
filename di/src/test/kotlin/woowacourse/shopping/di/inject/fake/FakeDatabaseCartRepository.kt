package woowacourse.shopping.di.inject.fake

import woowacourse.shopping.di.annotation.DatabaseMode
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

@DatabaseMode
class FakeDatabaseCartRepository : CartRepository {
    private val cartProducts = mutableListOf<CartProduct>()

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(
            CartProduct(
                0,
                name = "우테코 과자",
                price = 10_000,
                imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
                createdAt = System.currentTimeMillis(),
            ),
        )
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.removeAt(id.toInt())
    }
}
