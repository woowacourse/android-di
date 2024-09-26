package woowacourse.shopping.data.fake

import woowacourse.shopping.model.Product

interface IProductRepository {
    fun getAllProducts(): List<Product>
}
