package woowacourse.shopping.di.module

import android.content.Context
import woowacourse.shopping.di.container.InstanceContainer
import woowacourse.shopping.ui.cart.DateFormatter

class DefaultActivityModule(
    activityContext: Context,
    applicationModule: ApplicationModule,
    instanceContainer: InstanceContainer,
) : ActivityModule(activityContext, applicationModule, instanceContainer) {
    fun provideContext(): Context = activityContext

    fun provideDateFormatter(context: Context): DateFormatter = DateFormatter(context)
}
