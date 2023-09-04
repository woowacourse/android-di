package woowacourse.shopping.ui.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.main.viewholder.ProductViewHolder

class ProductAdapter(
    private val items: List<Product>,
    private val onClickProduct: (Product) -> Unit,
) : RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder.from(parent, onClickProduct)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
