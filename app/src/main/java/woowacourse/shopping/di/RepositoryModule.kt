package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

class RepositoryModule : Module {
    override fun provideDependencies(dependencyRegistry: DependencyRegistry) {
        dependencyRegistry.addInstance(ProductRepository::class, DefaultProductRepository())
        dependencyRegistry.addInstance(CartRepository::class, DefaultCartRepository())
    }
}
