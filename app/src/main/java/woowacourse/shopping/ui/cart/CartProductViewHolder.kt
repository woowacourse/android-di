package woowacourse.shopping.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.databinding.ItemCartProductBinding
import woowacourse.shopping.domain.model.CartProduct

class CartProductViewHolder(
    private val binding: ItemCartProductBinding,
    onClickDelete: (position: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.ivCartProductDelete.setOnClickListener {
            val position = adapterPosition
            onClickDelete(position)
        }
    }

    fun bind(cartProduct: CartProduct) {
        binding.item = cartProduct
    }

    companion object {
        fun from(
            parent: ViewGroup,
            onClickDelete: (position: Int) -> Unit,
        ): CartProductViewHolder {
            val binding =
                ItemCartProductBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            return CartProductViewHolder(binding, onClickDelete)
        }
    }
}
