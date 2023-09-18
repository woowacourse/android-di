package woowacourse.shopping.di.module

import com.ssu.di.module.Module
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.repository.ProductRepository

class ActivityModule : Module {
    fun provideProductRepository(): ProductRepository = DefaultProductRepository()
}