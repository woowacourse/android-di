package woowacourse.shopping.di.module

import android.content.Context
import woowacourse.shopping.annotation.ApplicationContext
import woowacourse.shopping.annotation.Provides
import woowacourse.shopping.module.DIActivityRetainedModule
import woowacourse.shopping.module.DIApplicationModule
import woowacourse.shopping.ui.cart.DateFormatter

class ActivityRetainedModule(parentModule: DIApplicationModule) :
    DIActivityRetainedModule(parentModule) {
    @Provides
    private fun provideDateFormatter(@ApplicationContext context: Context): DateFormatter {
        return DateFormatter(context)
    }
}
