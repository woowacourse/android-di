package woowacourse.shopping.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.model.CartProduct

class CartProductAdapter(
    items: List<CartProduct>,
    onClickDelete: (position: Long) -> Unit,
    private val dateFormatter: DateFormatter,
) : RecyclerView.Adapter<CartProductViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private val items: MutableList<CartProduct> = items.toMutableList()

    private val onClickDelete = { id: Long ->
        onClickDelete(id)
        removeItem(id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder.from(parent, dateFormatter, onClickDelete)
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    private fun removeItem(id: Long) {
        val position = items.indexOfFirst { it.id == id }
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}
