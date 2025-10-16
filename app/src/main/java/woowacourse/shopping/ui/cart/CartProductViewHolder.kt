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
    init {
        binding.itemListener =
            object : CartProductItemListener {
                override fun delete(id: Long) {
                    onClickDelete(id)
                }
            }
        binding.dateFormatter = dateFormatter
    }

    fun bind(product: CartProduct) {
        binding.item = product
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
