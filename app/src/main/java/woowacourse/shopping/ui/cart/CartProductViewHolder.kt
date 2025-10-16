package woowacourse.shopping.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.databinding.ItemCartProductBinding
import woowacourse.shopping.model.CartProduct

class CartProductViewHolder(
    private val binding: ItemCartProductBinding,
    private val dateFormatter: DateFormatter,
    private val onClickDelete: (cartProductId: Long) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(cartProduct: CartProduct) {
        binding.item = cartProduct
        val dateTime = dateFormatter.formatDate(cartProduct.createdAt)
        binding.tvCartProductCreatedAt.text = dateTime
        binding.ivCartProductDelete.setOnClickListener {
            onClickDelete(cartProduct.id)
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
            dateFormatter: DateFormatter,
            onClickDelete: (cartProductId: Long) -> Unit,
        ): CartProductViewHolder {
            val binding =
                ItemCartProductBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            return CartProductViewHolder(binding, dateFormatter, onClickDelete)
        }
    }
}
