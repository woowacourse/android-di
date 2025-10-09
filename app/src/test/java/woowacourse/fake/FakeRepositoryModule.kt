package woowacourse.fake

import woowacourse.shopping.core.DependencyModule
import woowacourse.shopping.domain.CartRepository

class FakeRepositoryModule : DependencyModule {
    val fakeProductRepository: CartRepository = FakeCartRepository()
}
