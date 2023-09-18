package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.hasydi.Module
import woowacourse.shopping.hasydi.annotation.ActivityContext
import woowacourse.shopping.hasydi.annotation.Inject
import woowacourse.shopping.hasydi.annotation.Singleton
import woowacourse.shopping.ui.cart.DateFormatter

class CartActivityModule : Module {

    override var context: Context? = null

    @Singleton
    fun provideDateFormatter(
        @ActivityContext @Inject
        context: Context,
    ) = DateFormatter(context)

    @Singleton
    @ActivityContext
    fun provideContext(): Context = context!!
}
