package woowacourse.shopping.ui.cart

import woowacourse.shopping.model.Product

object ProductFixture {
    fun Product() = Product("테스트", 1000, "")
    fun Products() =
        listOf(Product("테스트1", 1000, ""), Product("테스트2", 1000, ""), Product("테스트3", 1000, ""))
}