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

    private val onClickDelete = { id: Long ->
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

    fun updateItems(newItems: List<CartProduct>) { // 새로운 아이템 리스트로 어댑터 갱신
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    private fun removeItem(id: Long) {
        val position = items.indexOfFirst { it.id == id }
        if (position != -1) { // 해당 아이템이 리스트에 있는 경우만 삭제
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
