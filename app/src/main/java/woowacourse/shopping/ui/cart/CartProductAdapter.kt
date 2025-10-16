package woowacourse.shopping.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.model.CartProduct
import kotlin.collections.toMutableList

class CartProductAdapter(
    items: List<CartProduct>,
    onClickDelete: (position: Int) -> Unit,
    private val dateFormatter: DateFormatter,
) : RecyclerView.Adapter<CartProductViewHolder>() {
    private val items: MutableList<CartProduct> = items.toMutableList()

    private val onClickDelete = { position: Int ->
        onClickDelete(position)
        removeItem(items[position].id)
        notifyItemRemoved(position)
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
        items.filter { item -> item.id != id }
    }
}
