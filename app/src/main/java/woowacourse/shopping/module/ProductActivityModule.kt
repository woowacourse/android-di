package woowacourse.shopping.module

import com.example.di.DIModule
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.domain.repository.ProductRepository

class ProductActivityModule : DIModule {
    fun provideDefaultProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }
}