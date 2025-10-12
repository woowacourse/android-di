package woowacourse.shopping.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.model.Product

class CartProductAdapter(
    items: List<Product>,
    onClickDelete: (id: Long) -> Unit,
    private val dateFormatter: DateFormatter,
) : RecyclerView.Adapter<CartProductViewHolder>() {
    private val items: MutableList<Product> = items.toMutableList()

    private val onClickDelete = { id: Long ->
        onClickDelete(id)
        removeItem(id)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CartProductViewHolder = CartProductViewHolder.from(parent, dateFormatter, onClickDelete)

    override fun onBindViewHolder(
        holder: CartProductViewHolder,
        position: Int,
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    private fun removeItem(id: Long) {
        val position: Int = items.indexOfFirst { product: Product -> product.id == id }
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}
