package woowacourse.shopping

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

fun createProduct(
    name: String = "우테코 과자",
    price: Int = 10_000,
    imageUrl: String = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
) = Product(name, price, imageUrl)

fun createCartProduct(
    id: Long = System.currentTimeMillis(),
    name: String = "우테코 과자",
    price: Int = 10_000,
    imageUrl: String = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
    createdAt: Long = System.currentTimeMillis(),
) = CartProduct(id, name, price, imageUrl, createdAt)

fun getProducts(
    products: List<Product> = listOf(
        Product(
            name = "우테코 과자",
            price = 10_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
        ),
        Product(
            name = "우테코 쥬스",
            price = 8_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/52dca718-31c5-4f80-bafa-7e300d8c876a.jpg?h=700&w=700",
        ),
        Product(
            name = "우테코 아이스크림",
            price = 20_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/e703c53e-5d01-4b20-bd33-85b5e778e73f.jpg?h=700&w=700",
        ),
    ),
) = products

fun getCartProducts(
    products: List<CartProduct> = listOf(
        CartProduct(
            id = System.currentTimeMillis(),
            name = "우테코 과자",
            price = 10_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
            createdAt = System.currentTimeMillis(),
        ),
        CartProduct(
            id = System.currentTimeMillis(),
            name = "우테코 쥬스",
            price = 8_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/52dca718-31c5-4f80-bafa-7e300d8c876a.jpg?h=700&w=700",
            createdAt = System.currentTimeMillis(),
        ),
    ),
) = products

fun CartProduct.toProduct(): Product = Product(name, price, imageUrl)
