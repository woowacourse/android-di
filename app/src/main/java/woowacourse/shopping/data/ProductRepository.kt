package woowacourse.shopping.data

import woowacourse.shopping.model.Product

interface ProductRepository : Repository {
    fun getAllProducts(): List<Product>
}
