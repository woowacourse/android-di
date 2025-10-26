package woowacourse.shopping.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.domain.repository.ProductRepository
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object ScopedRepositoryModule {
    @ViewModelScoped
    @Provides
    @Named("productRepository")
    fun bindProductRepository(): ProductRepository = DefaultProductRepository()
}
