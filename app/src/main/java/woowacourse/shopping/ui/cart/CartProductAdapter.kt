package woowacourse.shopping.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.model.Product

class CartProductAdapter(
    items: List<Product>,
    onClickDelete: (position: Int) -> Unit,
    private val dateFormatter: DateFormatter,
) : RecyclerView.Adapter<CartProductViewHolder>() {
    private val items: MutableList<Product> = items.toMutableList()

    private val onClickDelete = { position: Int ->
        println("CartProductAdapter.onClickDelete: $position")
        println("CartProductAdapter: ${items[position]}")
        println("CartProductAdapter: items: $items")
        println("CartProductAdapter: size: ${items.size}")
        onClickDelete(items[position].id.toInt())
        removeItem(position)
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

    private fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}
