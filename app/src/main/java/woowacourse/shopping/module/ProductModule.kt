package woowacourse.shopping.module

import com.example.di.DIModule
import com.example.di.annotation.LifeCycle
import com.example.di.annotation.LifeCycleScope
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.domain.repository.ProductRepository

class ProductModule : DIModule {
    @LifeCycle(LifeCycleScope.VIEW_MODEL)
    fun provideDefaultProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }
}
