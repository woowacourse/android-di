package woowacourse.shopping.dummy

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

private val fakeProducts = List(10) {
    Product(name = "Hey ${it + 1}", price = 1000, imageUrl = "")
}

private val fakeCartProducts = List(10) {
    CartProduct(id = it.toLong(), name = "Hey ${it + 1}", price = 1000, imageUrl = "", createdAt = 0L)
}

fun createFakeProduct(id: Int) = fakeProducts[id]
fun createFakeProducts() = fakeProducts
fun createFakeCartProducts() = fakeCartProducts
fun createFakeCartProduct(id: Long) = CartProduct(id = id.toLong(), name = "Hey ${id.toLong() + 1}", price = 1000, imageUrl = "", createdAt = 0L)
