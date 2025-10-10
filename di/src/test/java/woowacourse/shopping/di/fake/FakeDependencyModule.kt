package woowacourse.shopping.di.fake

import woowacourse.shopping.core.DependencyModule
import woowacourse.shopping.di.annotation.Qualifier
import woowacourse.shopping.di.fake.repository.FakeCartRepository
import woowacourse.shopping.di.fake.repository.FakeProductRepository
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class FakeDependencyModule : DependencyModule {
    val stringProvider: String = "inject"

    @Qualifier("myCart")
    val myCartRepository: CartRepository = FakeCartRepository()

    @Qualifier("othersCart")
    val otherCartRepository: CartRepository = FakeCartRepository()

    val productRepository: ProductRepository = FakeProductRepository()
}
