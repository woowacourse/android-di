package woowacourse.shopping.ui.cart

import android.content.Context
import smile.di.Provides

class CartModule(
    val context: Context,
) {
    @Provides
    fun provideDateFormatter(): DateFormatter = DateFormatter(context)
}
