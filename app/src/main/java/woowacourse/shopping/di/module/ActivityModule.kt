package woowacourse.shopping.di.module

import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.repository.ProductRepository

class ActivityModule : Module {
    fun provideProductRepository(): ProductRepository = DefaultProductRepository()
}