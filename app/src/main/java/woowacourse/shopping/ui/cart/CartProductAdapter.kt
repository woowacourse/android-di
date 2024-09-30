package woowacourse.shopping.ui.cart

import android.annotation.SuppressLint
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
        val item = items[position]
        onClickDelete(item.id.toInt())
        removeItem(position)
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

    @SuppressLint("NotifyDataSetChanged")
    private fun removeItem(position: Int) {
        items.removeAt(position)
        notifyDataSetChanged()
    }
}
