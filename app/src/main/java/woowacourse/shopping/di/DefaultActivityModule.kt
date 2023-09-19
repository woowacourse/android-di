package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.ui.cart.DateFormatter

class DefaultActivityModule(
    activityContext: Context,
    applicationModule: ApplicationModule,
) : ActivityModule(activityContext, applicationModule) {
    fun provideDateFormatter(): DateFormatter = DateFormatter(activityContext)
}
