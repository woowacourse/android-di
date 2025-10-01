package woowacourse.shopping.di.module

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class RepositoryModule : Module {
    override fun register(container: DIContainer) {
        container.register(ProductRepository::class) { ProductRepositoryImpl() }
        container.register(CartRepository::class) { CartRepositoryImpl() }
    }
}
