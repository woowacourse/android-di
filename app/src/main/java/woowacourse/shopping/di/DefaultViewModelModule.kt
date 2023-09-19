package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository

class DefaultViewModelModule(
    context: Context,
    activityModule: ActivityModule,
) : ViewModelModule(context, activityModule) {
    fun provideProductRepository(): ProductRepository = DefaultProductRepository()
}
