package woowacourse.shopping.ui.products

import smile.di.Provides
import woowacourse.shopping.data.ProductRepository

class ProductsViewModelModule {
    @Provides
    fun provideProductRepository(): ProductRepository = ProductRepository()
}
