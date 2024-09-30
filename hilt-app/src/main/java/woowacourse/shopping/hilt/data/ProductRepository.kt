package woowacourse.shopping.hilt.data

import woowacourse.shopping.hilt.model.Product
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor() : ProductRepository {
    fun dummyProducts() =
        listOf(
            Product(
                id = -1,
                name = "우테코 과자",
                price = 10_000,
                imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
                createdAt = 0,
            ),
            Product(
                id = -1,
                name = "우테코 쥬스",
                price = 8_000,
                imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/52dca718-31c5-4f80-bafa-7e300d8c876a.jpg?h=700&w=700",
                createdAt = 0,
            ),
            Product(
                id = -1,
                name = "우테코 아이스크림",
                price = 20_000,
                imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/e703c53e-5d01-4b20-bd33-85b5e778e73f.jpg?h=700&w=700",
                createdAt = 0,
            ),
        )

    override suspend fun allProducts(): List<Product> {
        return dummyProducts()
    }
}

interface ProductRepository {
    suspend fun allProducts(): List<Product>
}
