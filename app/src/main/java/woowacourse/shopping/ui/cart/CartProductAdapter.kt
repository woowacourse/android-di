package woowacourse.shopping.ui.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import woowacourse.shopping.domain.model.CartProduct

class CartProductAdapter(
    onClickDelete: (position: Long) -> Unit,
) : ListAdapter<CartProduct, CartProductViewHolder>(diffUtil) {
    private val onClickDelete = { position: Int ->
        onClickDelete(currentList[position].id)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CartProductViewHolder {
        return CartProductViewHolder.from(parent, onClickDelete)
    }

    override fun onBindViewHolder(
        holder: CartProductViewHolder,
        position: Int,
    ) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int = currentList.size

//    private fun removeItem(position: Int) {
//        currentList.removeAt(position)
//        notifyItemRemoved(position)
//    }

    companion object {
        private val diffUtil: DiffUtil.ItemCallback<CartProduct> =
            object : DiffUtil.ItemCallback<CartProduct>() {
                override fun areItemsTheSame(
                    oldItem: CartProduct,
                    newItem: CartProduct,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: CartProduct,
                    newItem: CartProduct,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
