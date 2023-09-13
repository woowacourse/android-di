package woowacourse.shopping.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.model.Product

class CartProductAdapter(
    items: List<Product>,
    onClickDelete: (cartProductId: Long) -> Unit,
    private val dateFormatter: DateFormatter,
) : RecyclerView.Adapter<CartProductViewHolder>() {

    private val items: MutableList<Product> = items.toMutableList()

    private val onClickDelete = { cartProductId: Long ->
        onClickDelete(cartProductId)
        removeItem(cartProductId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder.from(parent, dateFormatter, onClickDelete)
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    private fun removeItem(cartProductId: Long) {
        val position = items.indexOfFirst { it.id == cartProductId }
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}
