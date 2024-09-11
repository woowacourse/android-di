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
    private var productId: Long = -1L

    init {
        binding.ivCartProductDelete.setOnClickListener {
            if (productId != -1L) {
                onClickDelete(productId)
            }
        }
    }

    fun bind(product: CartProduct) {
        productId = product.id
        binding.item = product
        binding.tvCartProductCreatedAt.text = dateFormatter.formatDate(product.createdAt)
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
