package woowacourse.shopping.di

import com.example.di.DiContainer
import com.example.di.Module
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

class RepositoryModule : Module {
    override fun provideInstance(registry: DiContainer) {
        registry.addInstance(
            ProductRepository::class,
            DefaultProductRepository(),
        )
        registry.addInstance(
            CartRepository::class,
            InMemoryCartRepository(),
        )
    }
}
