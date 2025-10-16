package woowacourse.shopping.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import woowacourse.shopping.domain.model.CartProduct

class CartProductAdapter(
    private val onClickDelete: CartProductViewHolder.OnClickDelete,
    private val dateFormatter: DateFormatter,
) : ListAdapter<CartProduct, CartProductViewHolder>(DiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CartProductViewHolder = CartProductViewHolder.from(parent, dateFormatter, onClickDelete)

    override fun onBindViewHolder(
        holder: CartProductViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<CartProduct>() {
                override fun areContentsTheSame(
                    oldItem: CartProduct,
                    newItem: CartProduct,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: CartProduct,
                    newItem: CartProduct,
                ): Boolean = oldItem.id == newItem.id
            }
    }
}
