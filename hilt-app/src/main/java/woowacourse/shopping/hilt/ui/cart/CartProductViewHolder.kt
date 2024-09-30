package woowacourse.shopping.hilt.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.hilt.databinding.ItemCartProductBinding
import woowacourse.shopping.hilt.model.Product

class CartProductViewHolder(
    private val binding: ItemCartProductBinding,
    private val dateFormatter: DateFormatter,
    onClickDelete: (id: Long) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.ivCartProductDelete.setOnClickListener {
            onClickDelete(binding.item?.id ?: return@setOnClickListener)
        }
    }

    fun bind(product: Product) {
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
