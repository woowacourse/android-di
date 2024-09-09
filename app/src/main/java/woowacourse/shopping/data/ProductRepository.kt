package woowacourse.shopping.data

import woowacourse.shopping.model.Product

interface ProductRepository {
    fun allProducts(): List<Product>
}
