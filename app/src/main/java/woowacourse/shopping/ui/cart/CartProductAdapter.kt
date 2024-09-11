package woowacourse.shopping.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.model.CartedProduct

class CartProductAdapter(
    items: List<CartedProduct>,
    onClickDelete: (id: Long) -> Unit,
    private val dateFormatter: DateFormatter,
) : RecyclerView.Adapter<CartProductViewHolder>() {
    private val items: MutableList<CartedProduct> = items.toMutableList()

    private val onClickDelete = { position: Int ->
        val id = items[position].id
        onClickDelete(id)
        removeItem(id)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CartProductViewHolder {
        return CartProductViewHolder.from(parent, dateFormatter, onClickDelete)
    }

    override fun onBindViewHolder(
        holder: CartProductViewHolder,
        position: Int,
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    private fun removeItem(id: Long) {
        val index = items.indexOfFirst { it.id == id }
        items.removeAt(index)
        notifyItemRemoved(index)
    }
}
