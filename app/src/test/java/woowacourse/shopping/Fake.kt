package woowacourse.shopping

import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class FakeCartRepository : CartRepository {
    private val cartProducts: MutableList<Product> =
        mutableListOf(
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

    override fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override fun getAllCartProducts(): List<Product> {
        return cartProducts.toList()
    }

    override fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}

class FakeProductRepository : ProductRepository {
    private val products: List<Product> =
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

    override fun getAllProducts(): List<Product> {
        return products
    }
}
