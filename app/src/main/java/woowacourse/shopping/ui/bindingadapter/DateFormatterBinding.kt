package woowacourse.shopping.ui.bindingadapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import woowacourse.shopping.ui.cart.DateFormatter

@BindingAdapter("setDateText")
fun TextView.setDateText(time: Long) {
    this.text = DateFormatter(this.context).formatDate(time)
}
