package woowacourse.shopping.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.databinding.ItemCartProductBinding
import woowacourse.shopping.domain.model.CartProduct

class CartProductViewHolder(
    private val binding: ItemCartProductBinding,
    private val dateFormatter: DateFormatter,
    onClickDelete: (position: Int, id: Long) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.ivCartProductDelete.setOnClickListener {
            val position = adapterPosition
            binding.item?.id?.let { onClickDelete(position, it) }
        }
    }

    fun bind(cartProduct: CartProduct) {
        binding.item = cartProduct
        binding.tvCartProductCreatedAt.text = dateFormatter.formatDate(cartProduct.addedAt)
    }

    companion object {
        fun from(
            parent: ViewGroup,
            dateFormatter: DateFormatter,
            onClickDelete: (Int, Long) -> Unit,
        ): CartProductViewHolder {
            val binding = ItemCartProductBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return CartProductViewHolder(binding, dateFormatter, onClickDelete)
        }
    }
}
