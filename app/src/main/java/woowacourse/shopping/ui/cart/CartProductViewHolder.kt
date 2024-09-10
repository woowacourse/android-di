package woowacourse.shopping.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.databinding.ItemCartProductBinding

class CartProductViewHolder(
    private val binding: ItemCartProductBinding,
    private val dateFormatter: DateFormatter,
    onClickDelete: (position: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.ivCartProductDelete.setOnClickListener {
            val position = adapterPosition
            onClickDelete(position)
        }
    }

    fun bind(cartProduct: CartProductEntity) {
        binding.item = cartProduct
        // TODO: Step2 - dateFormatter를 활용하여 상품이 담긴 날짜와 시간을 출력하도록 변경
    }

    companion object {
        fun from(
            parent: ViewGroup,
            dateFormatter: DateFormatter,
            onClickDelete: (position: Int) -> Unit,
        ): CartProductViewHolder {
            val binding =
                ItemCartProductBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            return CartProductViewHolder(binding, dateFormatter, onClickDelete)
        }
    }
}
