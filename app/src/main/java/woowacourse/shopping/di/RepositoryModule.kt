package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

class RepositoryModule : Module {
    override fun provideInstance(registry: DependencyRegistry) {
        registry.addInstance(
            ProductRepository::class,
            DefaultProductRepository(),
        )
        registry.addInstance(
            CartRepository::class,
            DefaultCartRepository(),
        )
    }
}
