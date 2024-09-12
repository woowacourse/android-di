package woowacourse.shopping.di

import com.example.di.DiContainer
import com.example.di.Module
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

class RepositoryModule : Module {
    override fun provideInstance(registry: DiContainer) {
        DiContainer.addInstance(
            ProductRepository::class,
            DefaultProductRepository(),
        )
        DiContainer.addInstance(
            CartRepository::class,
            DefaultCartRepository(),
        )
    }
}
