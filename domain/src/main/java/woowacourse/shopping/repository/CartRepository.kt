package woowacourse.shopping.repository

import woowacourse.shopping.model.Product

// TODO: Step2 - CartProductDao를 참조하도록 변경
interface CartRepository {
    suspend fun addCartProduct(product: Product)
    suspend fun getAllCartProducts(): List<Product>
    suspend fun deleteCartProduct(id: Int)
}
