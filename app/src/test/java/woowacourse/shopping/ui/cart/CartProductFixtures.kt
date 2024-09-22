package woowacourse.shopping.ui.cart

import woowacourse.shopping.model.Product

fun generateProducts(size: Int): List<Product> = List(size) { index -> generateSingleProduct(index + 1) }

fun generateSingleProduct(index: Int): Product = Product(index.toLong(), "Product$index", index * 1000, "image$index")
