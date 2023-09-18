package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.hashdi.Module
import woowacourse.shopping.hashdi.annotation.ActivityContext
import woowacourse.shopping.hashdi.annotation.Inject
import woowacourse.shopping.hashdi.annotation.Singleton
import woowacourse.shopping.ui.cart.DateFormatter

class CartActivityModule : Module {
    @Singleton
    fun provideDateFormatter(
        @ActivityContext @Inject
        context: Context,
    ) = DateFormatter(context)
}
