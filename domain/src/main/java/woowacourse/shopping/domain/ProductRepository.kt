package woowacourse.shopping.domain

import woowacourse.shopping.domain.model.Product

fun interface ProductRepository {
    fun getAllProducts(): List<Product>
}
