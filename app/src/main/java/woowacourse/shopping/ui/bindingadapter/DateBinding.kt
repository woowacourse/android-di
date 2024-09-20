package woowacourse.shopping.ui.bindingadapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import woowacourse.shopping.ui.cart.DateFormatter

@BindingAdapter("createdAt")
fun formatDate(
    view: TextView,
    createdAt: Long,
) {
    view.text = DateFormatter(view.context).formatDate(createdAt)
}
