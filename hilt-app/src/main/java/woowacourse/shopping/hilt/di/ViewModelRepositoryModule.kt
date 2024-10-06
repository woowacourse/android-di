package woowacourse.shopping.hilt.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import woowacourse.shopping.hilt.data.CartRepository
import woowacourse.shopping.hilt.data.DefaultCartRepository
import woowacourse.shopping.hilt.di.qualifier.ViewModelScopeCartQualifier

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelRepositoryModule {

    @Binds
    @ViewModelScoped
    @ViewModelScopeCartQualifier
    abstract fun bindCartRepository(
        repository: DefaultCartRepository
    ): CartRepository
}