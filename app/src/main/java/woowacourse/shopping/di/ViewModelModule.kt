package woowacourse.shopping.di

import com.re4rk.arkdi.ArkModule
import com.re4rk.arkdi.Singleton
import woowacourse.shopping.data.ProductSampleRepository
import woowacourse.shopping.repository.ProductRepository

@Suppress("unused")
class ViewModelModule(
    parentArkModule: ArkModule,
) : ArkModule(parentArkModule) {
    @Singleton
    fun provideProductRepository(): ProductRepository = ProductSampleRepository()
}
