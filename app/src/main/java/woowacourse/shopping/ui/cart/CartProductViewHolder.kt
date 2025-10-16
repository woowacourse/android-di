package woowacourse.shopping.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.databinding.ItemCartProductBinding
import woowacourse.shopping.domain.model.CartProduct

class CartProductViewHolder(
    private val binding: ItemCartProductBinding,
    private val dateFormatter: DateFormatter,
    onClickDelete: OnClickDelete,
) : RecyclerView.ViewHolder(binding.root) {
    private var id: Long? = null

    init {
        binding.ivCartProductDelete.setOnClickListener {
            id?.let(onClickDelete::onClick)
        }
    }

    fun bind(cartProduct: CartProduct) {
        id = cartProduct.id
        binding.item = cartProduct
        binding.tvCartProductCreatedAt.text = dateFormatter.formatDate(cartProduct.createdAt)
    }

    companion object {
        fun from(
            parent: ViewGroup,
            dateFormatter: DateFormatter,
            onClickDelete: OnClickDelete,
        ): CartProductViewHolder {
            val binding =
                ItemCartProductBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            return CartProductViewHolder(binding, dateFormatter, onClickDelete)
        }
    }

    fun interface OnClickDelete {
        fun onClick(id: Long)
    }
}
