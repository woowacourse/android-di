package woowacourse.shopping.application

import com.example.di.Module
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.domain.repository.ProductRepository

class ViewModelModule : Module {
    fun provideDefaultProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }
}
