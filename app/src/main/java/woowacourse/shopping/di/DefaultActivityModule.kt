package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.ui.cart.DateFormatter

class DefaultActivityModule(
    activityContext: Context,
) : ActivityModule(activityContext) {
    fun provideDateFormatter(): DateFormatter = DateFormatter(activityContext)
}
