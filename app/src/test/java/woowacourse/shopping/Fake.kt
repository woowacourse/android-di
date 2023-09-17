package woowacourse.shopping

import woowacourse.shopping.Dummy.cartProduct
import woowacourse.shopping.Dummy.product
import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

class FakeProductRepository : ProductRepository {
    private val products = mutableListOf(product)

    override fun getAllProducts(): List<Product> {
        return products
    }
}

class FakeCartRepository : CartRepository {
    private val cartProducts = mutableListOf(cartProduct)

    override suspend fun addCartProduct(product: Product) {
        cartProducts.add(product.toEntity().toCartProduct())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProducts.toList()
    }

    override suspend fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(0)
    }
}

object Dummy {
    val product = Product(
        name = "커피 볶는 아침",
        price = 1000,
        imageUrl = "https://mblogthumb-phinf.pstatic.net/MjAyMTA1MjRfMTY2/MDAxNjIxO" +
            "DY1NjUxNzgx.14dYk0xWShqxcYAV_RRYW0YFAUAQcViMnqvhHhnEycog.ZjtEf144lV6O" +
            "Hc4alIg7QEqxp1hP5HkJM4kkapyg01wg.JPEG.gorgeous10/IMG_3502.JPG?type=w800",
    )

    val cartProduct = CartProduct(
        Product(
            name = "커피 볶는 아침",
            price = 1000,
            imageUrl = "https://mblogthumb-phinf.pstatic.net/MjAyMTA1MjRfMTY2/MDAxNjIxO" +
                "DY1NjUxNzgx.14dYk0xWShqxcYAV_RRYW0YFAUAQcViMnqvhHhnEycog.ZjtEf144lV6O" +
                "Hc4alIg7QEqxp1hP5HkJM4kkapyg01wg.JPEG.gorgeous10/IMG_3502.JPG?type=w800",
        ),
        id = 1,
        createdAt = 1L,
    )
}
