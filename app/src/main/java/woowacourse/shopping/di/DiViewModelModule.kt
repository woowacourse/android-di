package woowacourse.shopping.di

import com.re4rk.arkdi.DiContainer
import com.re4rk.arkdi.Singleton
import woowacourse.shopping.data.ProductSampleRepository
import woowacourse.shopping.repository.ProductRepository

@Suppress("unused")
class DiViewModelModule(
    parentDiContainer: DiContainer,
) : DiContainer(parentDiContainer) {
    @Singleton
    fun provideProductRepository(): ProductRepository = ProductSampleRepository()
}
