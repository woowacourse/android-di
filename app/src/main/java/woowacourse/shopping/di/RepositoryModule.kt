package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.local.LocalCartRepository
import woowacourse.shopping.data.local.ShoppingDatabase
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

class RepositoryModule : Module {
    override fun provideDependencies(dependencyRegistry: DependencyRegistry) {
        dependencyRegistry.addInstance(ProductRepository::class, DefaultProductRepository())
        dependencyRegistry.addInstance(
            CartRepository::class,
            LocalCartRepository(ShoppingDatabase.instanceOrNull.cartProductDao()),
        )
    }
}
