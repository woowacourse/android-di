package woowacourse.shopping.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.ui.ProductClickListener

class CartProductAdapter(
    private val productClickListener: ProductClickListener,
    private val dateFormatter: DateFormatter,
) : ListAdapter<Product, CartProductViewHolder>(
        object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(
                oldItem: Product,
                newItem: Product,
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Product,
                newItem: Product,
            ): Boolean = oldItem == newItem
        },
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CartProductViewHolder = CartProductViewHolder.from(parent, dateFormatter, productClickListener)

    override fun onBindViewHolder(
        holder: CartProductViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }
}
