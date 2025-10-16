package woowacourse.shopping.app.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.databinding.ItemCartProductBinding
import woowacourse.shopping.model.Product

class CartProductViewHolder(
    private val binding: ItemCartProductBinding,
    private val dateFormatter: DateFormatter,
    onClickDelete: (id: Long) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    private var productId: Long? = null // ← 현재 바인딩된 아이템의 id 저장

    init {
        binding.ivCartProductDelete.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                productId?.let(onClickDelete)
            }
        }
    }

    fun bind(product: Product) {
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
