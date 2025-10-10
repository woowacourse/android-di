package woowacourse.shopping.ui.cart

import androidx.recyclerview.widget.DiffUtil
import woowacourse.shopping.model.Product

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(
        oldItem: Product,
        newItem: Product,
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Product,
        newItem: Product,
    ): Boolean = oldItem == newItem
}
