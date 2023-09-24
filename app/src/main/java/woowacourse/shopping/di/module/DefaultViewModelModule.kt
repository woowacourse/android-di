package woowacourse.shopping.di.module

import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository

class DefaultViewModelModule(
    applicationModule: ApplicationModule,
) : ViewModelModule(applicationModule) {
    fun provideProductRepository(): ProductRepository = DefaultProductRepository()
}
