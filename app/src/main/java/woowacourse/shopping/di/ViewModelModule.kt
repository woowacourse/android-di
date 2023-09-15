package woowacourse.shopping.di

import com.re4rk.arkdi.ArkContainer
import com.re4rk.arkdi.Singleton
import woowacourse.shopping.data.ProductSampleRepository
import woowacourse.shopping.repository.ProductRepository

@Suppress("unused")
class ViewModelModule(
    parentArkContainer: ArkContainer,
) : ArkContainer(parentArkContainer) {
    @Singleton
    fun provideProductRepository(): ProductRepository = ProductSampleRepository()
}
