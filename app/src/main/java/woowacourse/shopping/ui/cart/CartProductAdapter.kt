package woowacourse.shopping.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import woowacourse.shopping.model.CartProduct

class CartProductAdapter(
    private val onClickDelete: (id: Long) -> Unit,
    private val dateFormatter: DateFormatter,
) : ListAdapter<CartProduct, CartProductViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CartProductViewHolder {
        return CartProductViewHolder.from(parent, dateFormatter, onClickDelete)
    }

    override fun onBindViewHolder(
        holder: CartProductViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil =
            object : DiffUtil.ItemCallback<CartProduct>() {
                override fun areItemsTheSame(
                    oldItem: CartProduct,
                    newItem: CartProduct,
                ): Boolean = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: CartProduct,
                    newItem: CartProduct,
                ): Boolean = oldItem == newItem
            }
    }
}
