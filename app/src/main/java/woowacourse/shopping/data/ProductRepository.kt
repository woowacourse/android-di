package woowacourse.shopping.data

import woowacourse.shopping.model.Product

interface ProductRepository {
    fun products(): List<Product>
}
