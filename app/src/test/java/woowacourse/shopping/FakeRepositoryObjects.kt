package woowacourse.shopping

import woowacourse.shopping.domain.model.CartProduct
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

val fakeProducts: List<Product> =
    listOf(
        Product(
            name = "우테코 김치",
            price = 5_900,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700"
        ),
        Product(
            name = "우테코 생수",
            price = 2_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/52dca718-31c5-4f80-bafa-7e300d8c876a.jpg?h=700&w=700"
        ),
        Product(
            name = "우테코 삼겹살",
            price = 20_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/e703c53e-5d01-4b20-bd33-85b5e778e73f.jpg?h=700&w=700"
        ),
    )

val fakeCartProducts: List<CartProduct> =
    listOf(
        CartProduct(
            id = 0L,
            name = "우테코 김치",
            price = 5_900,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
            addedAt = 0L,
        ),
        CartProduct(
            id = 1L,
            name = "우테코 생수",
            price = 2_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/52dca718-31c5-4f80-bafa-7e300d8c876a.jpg?h=700&w=700",
            addedAt = 1L,
        ),
        CartProduct(
            id = 2L,
            name = "우테코 삼겹살",
            price = 20_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/e703c53e-5d01-4b20-bd33-85b5e778e73f.jpg?h=700&w=700",
            addedAt = 2L
        ),
    )

class FakeCartRepository(
    private val cartProducts: MutableList<CartProduct>,
) : CartRepository {

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(
            CartProduct(
                id = 0,
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
                addedAt = 0L,
            )
        )
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }
    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.first {id == it.id}
    }
}

class FakeProductRepository(
    private val products: List<Product>,
) : ProductRepository {
    override fun getAllProducts(): List<Product> {
        return products
    }
}

interface FakeRepository

class FakeRepositoryImpl : FakeRepository
