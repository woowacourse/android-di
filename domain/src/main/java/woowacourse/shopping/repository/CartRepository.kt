package woowacourse.shopping.repository

import woowacourse.shopping.model.Product

// TODO: Step2 - CartProductDao를 참조하도록 변경
interface CartRepository {
    fun addCartProduct(product: Product)
    fun getAllCartProducts(): List<Product>
    fun deleteCartProduct(id: Int)
}
