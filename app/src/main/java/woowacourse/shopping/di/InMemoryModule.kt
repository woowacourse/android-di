package woowacourse.shopping.di

import com.kmlibs.supplin.annotations.Abstract
import com.kmlibs.supplin.annotations.Module
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.InMemoryProductRepositoryImpl

@Module
abstract class InMemoryModule {
    @Abstract
    abstract fun provideProductRepository(impl: InMemoryProductRepositoryImpl): ProductRepository

    @Abstract
    abstract fun provideInMemoryCartRepository(impl: InMemoryCartRepository): CartRepository
}
