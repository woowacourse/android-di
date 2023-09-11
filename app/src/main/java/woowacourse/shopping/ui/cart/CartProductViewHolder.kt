package woowacourse.shopping.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.databinding.ItemCartProductBinding
import woowacourse.shopping.model.CartProduct

class CartProductViewHolder(
    private val binding: ItemCartProductBinding,
    private val dateFormatter: DateFormatter,
    private val onClickDelete: (id: Long) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cartProduct: CartProduct) {
        binding.item = cartProduct.product
        binding.tvCartProductCreatedAt.text = dateFormatter.formatDate(cartProduct.createdAt)
        binding.ivCartProductDelete.setOnClickListener {
            onClickDelete(cartProduct.cartProductId)
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
            dateFormatter: DateFormatter,
            onClickDelete: (id: Long) -> Unit,
        ): CartProductViewHolder {
            val binding = ItemCartProductBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return CartProductViewHolder(binding, dateFormatter, onClickDelete)
        }
    }
}
