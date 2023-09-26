package woowacourse.shopping.ui.cart

import androidx.recyclerview.widget.DiffUtil
import woowacourse.shopping.model.CartProduct

object CartDiffUtilCallback : DiffUtil.ItemCallback<CartProduct>() {
    override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean =
        oldItem == newItem
}
