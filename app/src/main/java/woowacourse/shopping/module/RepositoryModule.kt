package woowacourse.shopping.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.data.LocalCartRepository
import woowacourse.shopping.data.ProductRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun cartRepository(
        cartRepository: LocalCartRepository
    ): CartRepository

}

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelRepositoryModule {
    @Binds
    @ViewModelScoped
    abstract fun productRepository(
        productRepository: InMemoryProductRepository
    ): ProductRepository

}
