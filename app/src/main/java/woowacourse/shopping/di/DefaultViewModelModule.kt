package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository

class DefaultViewModelModule(
    applicationModule: ApplicationModule,
) : ViewModelModule(applicationModule) {
    fun provideProductRepository(): ProductRepository = DefaultProductRepository()
}
