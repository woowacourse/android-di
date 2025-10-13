package woowacourse.shopping.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import woowacourse.shopping.domain.model.Product

class CartProductAdapter(
    private val onClickDelete: CartProductViewHolder.OnClickDelete,
    private val dateFormatter: DateFormatter,
) : ListAdapter<Product, CartProductViewHolder>(DiffUtil) {
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
            object : DiffUtil.ItemCallback<Product>() {
                override fun areContentsTheSame(
                    oldItem: Product,
                    newItem: Product,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: Product,
                    newItem: Product,
                ): Boolean = oldItem.id == newItem.id
            }
    }
}
