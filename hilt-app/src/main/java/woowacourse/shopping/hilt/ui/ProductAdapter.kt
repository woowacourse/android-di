package woowacourse.shopping.hilt.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import woowacourse.shopping.hilt.model.Product
import woowacourse.shopping.hilt.utils.ItemDiffCallback

class ProductAdapter(
    private val onClickProduct: (product: Product) -> Unit,
) : ListAdapter<Product, ProductViewHolder>(productComparator) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ProductViewHolder {
        return ProductViewHolder.from(parent, onClickProduct)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int,
    ) {
        holder.bind(currentList[position])
    }

    override fun getItemId(position: Int): Long = currentList[position].id

    companion object {
        private val productComparator =
            ItemDiffCallback<Product>(
                onItemsTheSame = { oldItem, newItem -> oldItem == newItem },
                onContentsTheSame = { oldItem, newItem -> oldItem == newItem },
            )
    }
}
