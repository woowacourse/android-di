package woowacourse.shopping.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import woowacourse.shopping.model.CartProduct

class CartProductAdapter(
    private val dateFormatter: DateFormatter,
    onClickDelete: (id: Long) -> Unit,
) : ListAdapter<CartProduct, CartProductViewHolder>(CartDiffUtilCallback) {
    private val onClickDelete = { id: Long ->
        onClickDelete(id)
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = getItem(position).id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder.from(parent, dateFormatter, onClickDelete)
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
