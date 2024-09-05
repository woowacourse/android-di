package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class AppModule : DIModule {
    override fun register(container: DIContainer) {
        container.registerMapping(ProductRepository::class, DefaultProductRepository::class)
        container.registerMapping(CartRepository::class, DefaultCartRepository::class)
    }
}
