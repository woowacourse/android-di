package woowacourse.shopping.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.data.model.Product
import woowacourse.shopping.presentation.databinding.ItemProductBinding

class ProductViewHolder(
    private val binding: ItemProductBinding,
    onClickProduct: (Product) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            onClickProduct(binding.item ?: return@setOnClickListener)
        }
    }

    fun bind(product: Product) {
        binding.item = product
    }

    companion object {
        fun from(parent: ViewGroup, onClickProduct: (Product) -> Unit): ProductViewHolder {
            val binding =
                ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ProductViewHolder(binding, onClickProduct)
        }
    }
}
