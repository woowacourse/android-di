package woowacourse.shopping.di.fake

import woowacourse.shopping.core.DependencyModule
import woowacourse.shopping.di.fake.repository.FakeCartRepository
import woowacourse.shopping.di.fake.repository.FakeProductRepository
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class FakeDependencyModule : DependencyModule {
    val stringProvider: String = "inject"
    val cartRepository: CartRepository = FakeCartRepository()
    val productRepository: ProductRepository = FakeProductRepository()
}
