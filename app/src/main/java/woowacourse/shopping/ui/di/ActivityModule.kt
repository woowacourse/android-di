package woowacourse.shopping.ui.di

import android.content.Context
import io.hyemdooly.di.Module
import io.hyemdooly.di.annotation.Singleton
import woowacourse.shopping.ui.cart.DateFormatter

class ActivityModule(parentModule: Module, private val activityContext: Context) :
    Module(parentModule) {
    @Singleton
    fun provideContext() = activityContext

    @Singleton
    fun provideFormatter(context: Context) = DateFormatter(context)
}
