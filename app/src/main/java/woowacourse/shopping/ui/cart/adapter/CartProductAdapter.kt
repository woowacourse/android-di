package woowacourse.shopping.ui.cart.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.cart.viewholder.CartProductViewHolder
import java.util.UUID

class CartProductAdapter(
    items: List<CartProduct>,
    onClickDelete: (id: UUID) -> Unit,
    private val dateFormatter: DateFormatter,
) : RecyclerView.Adapter<CartProductViewHolder>() {

    private val items: MutableList<CartProduct> = items.toMutableList()

    private val onClickDelete = { position: Int, id: UUID ->
        onClickDelete(id)
        removeItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder.from(parent, dateFormatter, onClickDelete)
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    private fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}
