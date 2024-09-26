package woowacourse.shopping.hilt.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.hilt.data.CartRepository
import woowacourse.shopping.hilt.data.DefaultCartRepository
import woowacourse.shopping.hilt.data.ProductRepository
import woowacourse.shopping.hilt.data.ProductRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(repository: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(repository: DefaultCartRepository): CartRepository
}
