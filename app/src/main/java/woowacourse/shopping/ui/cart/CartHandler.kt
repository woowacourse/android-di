package woowacourse.shopping.ui.cart

interface CartHandler {
    fun onClickDelete(
        id: Long,
        position: Int,
    )
}
