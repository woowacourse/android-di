package woowacourse.shopping.hilt.di

import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn
import woowacourse.shopping.hilt.data.CartRepository
import woowacourse.shopping.hilt.di.qualifier.ViewModelScopeCartQualifier
import woowacourse.shopping.hilt.fake.StubCartRepository

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [ViewModelRepositoryModule::class]
)
abstract class TestViewModelRepositoryModule {
    @Binds
    @ViewModelScoped
    @ViewModelScopeCartQualifier
    abstract fun bindCartRepository(
        repository: StubCartRepository
    ): CartRepository
}