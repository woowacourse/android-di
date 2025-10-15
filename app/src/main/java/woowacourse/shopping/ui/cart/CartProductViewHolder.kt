package woowacourse.shopping.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.databinding.ItemCartProductBinding
import woowacourse.shopping.ui.model.CartUiModel

class CartProductViewHolder(
    private val binding: ItemCartProductBinding,
    private val dateFormatter: DateFormatter,
    onClickDelete: (id: Long) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    private var cartItemId: Long? = null

    init {
        binding.ivCartProductDelete.setOnClickListener {
            cartItemId?.let { id ->
                onClickDelete(id)
            }
        }
    }

    fun bind(cartItem: CartUiModel) {
        cartItemId = cartItem.id
        binding.item = cartItem
        binding.tvCartProductCreatedAt.text = dateFormatter.formatDate(cartItem.createdAt)
    }

    companion object {
        fun from(
            parent: ViewGroup,
            dateFormatter: DateFormatter,
            onClickDelete: (id: Long) -> Unit,
        ): CartProductViewHolder {
            val binding =
                ItemCartProductBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            return CartProductViewHolder(binding, dateFormatter, onClickDelete)
        }
    }
}
