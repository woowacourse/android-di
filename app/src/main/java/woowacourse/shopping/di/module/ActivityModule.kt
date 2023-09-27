package woowacourse.shopping.di.module

import android.content.Context
import woowacourse.shopping.Module
import woowacourse.shopping.annotation.ActivityContext
import woowacourse.shopping.annotation.Singleton
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.ui.cart.DateFormatter

class ActivityModule(private val context: Context) : Module {

    @ActivityContext
    fun provideContext(): Context = context

    fun provideDateFormatter(@ActivityContext context: Context): DateFormatter =
        DateFormatter(context)

    @Singleton
    fun provideProductRepository(): ProductRepository = DefaultProductRepository()
}
