package woowacourse.shopping.application.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.domain.repository.ProductRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProductRepositoryModule {
    @Binds
    @ViewModelScoped
    abstract fun provideDefaultProductRepository(defaultProductRepository: DefaultProductRepository): ProductRepository
}
