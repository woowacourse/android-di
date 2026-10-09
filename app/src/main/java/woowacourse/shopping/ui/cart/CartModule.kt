package woowacourse.shopping.ui.cart

import android.content.Context
import smile.di.Provides
import woowacourse.shopping.data.CartRepository

class CartModule(
    val context: Context
) {
    @Provides
    fun provideDateFormatter(): DateFormatter = DateFormatter(context)
}
