package woowacourse.shopping.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import woowacourse.shopping.model.Product

class CartProductAdapter(
    onClickDelete: (productId: Int) -> Unit,
    private val dateFormatter: DateFormatter,
) : ListAdapter<Product, CartProductViewHolder>(ProductDiffCallback()) {
    private val onClickDelete = { position: Int ->
        val product = getItem(position)
        onClickDelete(product.id)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CartProductViewHolder = CartProductViewHolder.from(parent, dateFormatter, onClickDelete)

    override fun onBindViewHolder(
        holder: CartProductViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }
}
