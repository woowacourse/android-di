package woowacourse.shopping.di

import org.library.haeum.di.Module
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.data.repository.ProductRepository

class ViewModelModule : Module() {
    fun provideProductRepository(): ProductRepository = DefaultProductRepository
}
