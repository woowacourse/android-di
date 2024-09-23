package woowacourse.shopping.di

import com.kmlibs.supplin.annotations.Abstract
import com.kmlibs.supplin.annotations.Concrete
import com.kmlibs.supplin.annotations.Module
import woowacourse.shopping.data.InMemoryProductRepositoryImpl
import woowacourse.shopping.data.ProductRepository

@Module
object ProductModule {
    @Concrete
    fun provideProductRepository(): ProductRepository = InMemoryProductRepositoryImpl()
}
