package woowacourse.shopping.hilt.di

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import woowacourse.shopping.hilt.data.CartRepository
import woowacourse.shopping.hilt.data.ProductRepository
import woowacourse.shopping.hilt.fake.StubCartRepository
import woowacourse.shopping.hilt.fake.StubProductRepository
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class TestRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(repository: StubProductRepository): ProductRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(repository: StubCartRepository): CartRepository
}
