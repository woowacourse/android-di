package woowacourse.shopping.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.model.CartProduct

class CartProductAdapter(
    items: List<CartProduct>,
    onClickDelete: (id: Long) -> Unit,
    private val dateFormatter: DateFormatter,
) : RecyclerView.Adapter<CartProductViewHolder>() {

    private val items: MutableList<CartProduct> = items.toMutableList()

    private val onClickDelete = { item: CartProduct ->
        onClickDelete(item.id)
        removeItem(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder.from(parent, dateFormatter, onClickDelete)
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    private fun removeItem(item: CartProduct) {
        val position = items.indexOf(item)
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}
