package woowacourse.shopping.di.module

import android.content.Context
import woowacourse.shopping.ui.cart.DateFormatter

object CartActivityModule {
    fun provideDateFormatter(context: Context): DateFormatter {
        return DateFormatter(context)
    }
}
