package woowacourse.shopping.di.module

import android.content.Context
import com.now.di.Module
import woowacourse.shopping.ui.cart.DateFormatter

class CartActivityModule(private val context: Context) : Module {
    fun provideDateFormatter(): DateFormatter {
        return DateFormatter(context)
    }
}
