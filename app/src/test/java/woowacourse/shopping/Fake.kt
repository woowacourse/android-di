package woowacourse.shopping

import woowacourse.shopping.Dummy.product
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
    private val products = mutableListOf(product)

    override fun addCartProduct(product: Product) {
        products.add(product)
    }

    override fun getAllCartProducts(): List<Product> {
        return products.toList()
    }

    override fun deleteCartProduct(id: Int) {
        products.removeAt(0)
    }
}

object Dummy {
    val product = Product(
        name = "커피 볶는 아침",
        price = 1000,
        imageUrl = "https://mblogthumb-phinf.pstatic.net/MjAyMTA1MjRfMTY2/MDAxNjIxODY1NjUxNzgx.14dYk0xWShqxcYAV_RRYW0YFAUAQcViMnqvhHhnEycog.ZjtEf144lV6OHc4alIg7QEqxp1hP5HkJM4kkapyg01wg.JPEG.gorgeous10/IMG_3502.JPG?type=w800",
    )
}
