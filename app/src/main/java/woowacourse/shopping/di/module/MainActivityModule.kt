package woowacourse.shopping.di.module

import com.now.di.Module
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.repository.ProductRepository

class MainActivityModule : Module {
    fun provideProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }
}
