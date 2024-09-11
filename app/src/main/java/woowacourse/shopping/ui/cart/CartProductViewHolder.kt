package woowacourse.shopping.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.databinding.ItemCartProductBinding
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.ui.CartHandler

class CartProductViewHolder(
    private val binding: ItemCartProductBinding,
    private val cartHandler: CartHandler,
    private val dateFormatter: DateFormatter,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(cartProduct: CartProduct) {
        binding.item = cartProduct
        binding.handler = cartHandler
        binding.position = adapterPosition
        // TODO: Step2 - dateFormatter를 활용하여 상품이 담긴 날짜와 시간을 출력하도록 변경
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
