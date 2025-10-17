package woowacourse.shopping.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import woowacourse.shopping.model.Product

class CartProductAdapter(
    onClickDeleteById: (productId: Int) -> Unit,
    private val dateFormatter: DateFormatter,
) : ListAdapter<Product, CartProductViewHolder>(ProductDiffCallback()) {
    private val onClickDeleteByPosition = { position: Int ->
        val product = getItem(position)
        onClickDeleteById(product.id)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CartProductViewHolder = CartProductViewHolder.from(parent, dateFormatter, onClickDeleteByPosition)

    override fun onBindViewHolder(
        holder: CartProductViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }
}
