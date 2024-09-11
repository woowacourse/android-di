package woowacourse.shopping.di.module

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class RepositoryModule : DIModule {
    override fun register(container: DIContainer) {
        val cartProductDao = DIContainer.resolve(CartProductDao::class)

        DIContainer.registerInstance(
            CartRepository::class,
            DefaultCartRepository(cartProductDao),
        )

        DIContainer.registerMapping(ProductRepository::class, DefaultProductRepository::class)
    }
}
