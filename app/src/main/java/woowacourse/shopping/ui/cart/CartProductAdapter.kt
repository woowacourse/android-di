package woowacourse.shopping.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import woowacourse.shopping.model.Product

class CartProductAdapter(
    private val dateFormatter: DateFormatter,
    private val listener: CartProductClickListener,
) : ListAdapter<Product, CartProductViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CartProductViewHolder = CartProductViewHolder.from(parent, dateFormatter)

    override fun onBindViewHolder(
        holder: CartProductViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        holder.bind(item, listener)
    }

    companion object {
        private val diffUtil =
            object : DiffUtil.ItemCallback<Product>() {
                override fun areItemsTheSame(
                    oldItem: Product,
                    newItem: Product,
                ): Boolean = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: Product,
                    newItem: Product,
                ): Boolean = oldItem == newItem
            }
    }
}
