package woowacourse.shopping.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import woowacourse.shopping.model.CartProduct

class CartProductAdapter(
    onClickDelete: (id: Long) -> Unit,
    private val dateFormatter: DateFormatter,
) : ListAdapter<CartProduct, CartProductViewHolder>(DIFF_CALLBACK) {
    private val onClickDelete = { id: Long ->
        onClickDelete(id)
        removeItem(id)
    }

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

    private fun removeItem(id: Long) {
        val newItems = currentList.filter { it.id != id }
        submitList(newItems)
    }

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<CartProduct>() {
                override fun areItemsTheSame(
                    oldItem: CartProduct,
                    newItem: CartProduct,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: CartProduct,
                    newItem: CartProduct,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
