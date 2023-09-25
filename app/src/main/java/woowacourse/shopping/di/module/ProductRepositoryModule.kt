package woowacourse.shopping.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.model.ProductRepository

@Module
@InstallIn(ActivityComponent::class)
abstract class ProductRepositoryModule {

    @Binds
    abstract fun bindProductRepository(
        defaultProductRepository: DefaultProductRepository
    ): ProductRepository
}
