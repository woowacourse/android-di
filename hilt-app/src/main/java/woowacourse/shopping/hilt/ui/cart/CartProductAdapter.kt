package woowacourse.shopping.hilt.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import woowacourse.shopping.hilt.model.Product
import woowacourse.shopping.hilt.utils.ItemDiffCallback

class CartProductAdapter(
    private val onClickDelete: (id: Long) -> Unit,
    private val dateFormatter: DateFormatter,
) : ListAdapter<Product, CartProductViewHolder>(productComparator) {
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
        holder.bind(currentList[position])
    }

    companion object {
        private val productComparator =
            ItemDiffCallback<Product>(
                onItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
                onContentsTheSame = { oldItem, newItem -> oldItem == newItem },
            )
    }
}
