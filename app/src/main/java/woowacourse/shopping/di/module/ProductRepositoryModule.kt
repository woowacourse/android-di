package woowacourse.shopping.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.model.ProductRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProductRepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindProductRepository(
        defaultProductRepository: DefaultProductRepository
    ): ProductRepository
}
