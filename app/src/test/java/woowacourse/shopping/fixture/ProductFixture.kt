package woowacourse.shopping.fixture

import woowacourse.shopping.model.Product

object ProductFixture {
    fun getProduct(id: Int = 1) = Product("Product$id", id * 1000, "product$id.com", 0L)

    fun getProducts(ids: List<Int>) = ids.map { getProduct(it) }
}
