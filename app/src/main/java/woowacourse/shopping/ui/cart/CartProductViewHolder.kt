package woowacourse.shopping.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.databinding.ItemCartProductBinding
import woowacourse.shopping.model.CartProduct

class CartProductViewHolder(
    private val binding: ItemCartProductBinding,
    private val cartHandler: CartHandler,
    private val dateFormatter: DateFormatter,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(cartProduct: CartProduct) {
        binding.item = cartProduct
        binding.handler = cartHandler
        binding.position = adapterPosition
        binding.createdAt = dateFormatter.formatDate(cartProduct.createdAt)
    }

    companion object {
        fun from(
            parent: ViewGroup,
            cartHandler: CartHandler,
            dateFormatter: DateFormatter,
        ): CartProductViewHolder {
            val binding =
                ItemCartProductBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            return CartProductViewHolder(binding, cartHandler, dateFormatter)
        }
    }
}
