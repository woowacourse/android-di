package woowacourse.shopping.ui

interface CartHandler {
    fun onClickDelete(
        id: Long,
        position: Int,
    )
}
