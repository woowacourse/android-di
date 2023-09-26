package woowacourse.shopping.di.module

import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.container.InstanceContainer

class DefaultViewModelModule(
    applicationModule: ApplicationModule,
    instanceContainer: InstanceContainer,
) : ViewModelModule(applicationModule, instanceContainer) {
    fun provideProductRepository(): ProductRepository = DefaultProductRepository()
}
