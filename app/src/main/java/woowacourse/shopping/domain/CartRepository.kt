package woowacourse.shopping.domain

import woowacourse.shopping.ui.model.CartUiModel

interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<CartUiModel>

    suspend fun deleteCartProduct(id: Long)
}
