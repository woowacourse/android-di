package woowacourse.shopping.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.databinding.ItemCartProductBinding
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.ui.ProductClickListener

class CartProductViewHolder private constructor(
    private val binding: ItemCartProductBinding,
    private val dateFormatter: DateFormatter,
    productClickListener: ProductClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    private var product: Product? = null

    init {
        binding.ivCartProductDelete.setOnClickListener {
            product?.let { productClickListener.onProductDeleteClicked(it.id) }
        }
    }

    fun bind(product: Product) {
        this.product = product
        binding.item = product
        // TODO: Step2 - dateFormatter를 활용하여 상품이 담긴 날짜와 시간을 출력하도록 변경
    }

    companion object {
        fun from(
            parent: ViewGroup,
            dateFormatter: DateFormatter,
            productClickListener: ProductClickListener,
        ): CartProductViewHolder {
            val binding =
                ItemCartProductBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            return CartProductViewHolder(binding, dateFormatter, productClickListener)
        }
    }
}
