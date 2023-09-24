package woowacourse.shopping.di.module

import android.content.Context
import woowacourse.shopping.ui.cart.DateFormatter

class DefaultActivityModule(
    activityContext: Context,
    applicationModule: ApplicationModule,
) : ActivityModule(activityContext, applicationModule) {
    fun provideContext(): Context = activityContext

    fun provideDateFormatter(context: Context): DateFormatter = DateFormatter(context)
}
