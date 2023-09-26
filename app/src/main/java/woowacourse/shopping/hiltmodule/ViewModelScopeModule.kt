package woowacourse.shopping.hiltmodule

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.repository.ProductRepository

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelScopeModule {
    @Provides
    @ViewModelScoped
    fun provideProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }
}
