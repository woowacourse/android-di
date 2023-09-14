package woowacourse.shopping.di

import com.re4rk.arkdi.DiModule
import com.re4rk.arkdi.Singleton
import woowacourse.shopping.data.ProductSampleRepository
import woowacourse.shopping.repository.ProductRepository

@Suppress("unused")
class DiViewModelModule : DiModule() {
    @Singleton
    fun provideProductRepository(): ProductRepository = ProductSampleRepository()
}
