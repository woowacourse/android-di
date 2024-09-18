package woowacourse.shopping.di

import com.kmlibs.supplin.annotations.Abstract
import com.kmlibs.supplin.annotations.Module
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.InMemoryProductRepositoryImpl
import woowacourse.shopping.data.ProductRepository

@Module
interface InMemoryModule {
    @Abstract
    fun provideProductRepository(impl: InMemoryProductRepositoryImpl): ProductRepository

    @Abstract
    fun provideInMemoryCartRepository(impl: InMemoryCartRepository): CartRepository
}
