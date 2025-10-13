package woowacourse.shopping.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.databinding.ItemCartProductBinding
import woowacourse.shopping.domain.model.Product

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

    fun bind(product: Product) {
        id = product.id
        binding.item = product
        binding.tvCartProductCreatedAt.text = dateFormatter.formatDate(product.createdAt)
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
