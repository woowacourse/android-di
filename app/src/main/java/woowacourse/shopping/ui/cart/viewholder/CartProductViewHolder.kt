package woowacourse.shopping.ui.cart.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.databinding.ItemCartProductBinding
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.ui.cart.DateFormatter
import java.util.UUID

class CartProductViewHolder(
    private val binding: ItemCartProductBinding,
    private val dateFormatter: DateFormatter,
    private val onClickDelete: (position: Int, id: UUID) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(product: CartProduct) {
        binding.item = product
        binding.tvCartProductCreatedAt.text = dateFormatter.formatDate(product.createdAt)
        binding.onClickRemove = ::deleteItem
    }

    private fun deleteItem(id: UUID) {
        onClickDelete(adapterPosition, id)
    }

    companion object {
        fun from(
            parent: ViewGroup,
            dateFormatter: DateFormatter,
            onClickDelete: (position: Int, id: UUID) -> Unit,
        ): CartProductViewHolder {
            val binding = ItemCartProductBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return CartProductViewHolder(binding, dateFormatter, onClickDelete)
        }
    }
}
