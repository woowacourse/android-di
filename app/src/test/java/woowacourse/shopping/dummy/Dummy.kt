package woowacourse.shopping.dummy

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

private val fakeProducts = List(10) {
    Product(name = "Hey ${it + 1}", price = 1000, imageUrl = "")
}

fun createFakeProduct(id: Int) = fakeProducts[id]
fun createFakeProducts() = fakeProducts
fun createFakeCartProducts() = fakeProducts.map { CartProduct(it.name, it.price, it.imageUrl, 1) }
