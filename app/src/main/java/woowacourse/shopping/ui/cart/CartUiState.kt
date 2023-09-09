package woowacourse.shopping.ui.cart

import woowacourse.shopping.model.CartProduct

data class CartUiState(
    var cartProducts: List<CartProduct> = listOf(),
    val onDelete: (id: Long) -> Unit = {}
)
