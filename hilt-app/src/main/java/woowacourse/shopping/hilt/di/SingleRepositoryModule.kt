package woowacourse.shopping.hilt.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.hilt.data.CartRepository
import woowacourse.shopping.hilt.data.DefaultCartRepository
import woowacourse.shopping.hilt.data.ProductRepository
import woowacourse.shopping.hilt.data.ProductRepositoryImpl
import woowacourse.shopping.hilt.di.qualifier.SingleCartQualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SingleRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(repository: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    @SingleCartQualifier
    abstract fun bindCartRepository(
        repository: DefaultCartRepository
    ): CartRepository
}

