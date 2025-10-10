package woowacourse.shopping.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.ui.model.CartUiModel

class CartProductAdapter(
    items: List<CartUiModel>,
    onClickDelete: (position: Int) -> Unit,
    private val dateFormatter: DateFormatter,
) : RecyclerView.Adapter<CartProductViewHolder>() {
    private val items: MutableList<CartUiModel> = items.toMutableList()

    private val onClickDelete = { position: Int ->
        onClickDelete(position)
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
