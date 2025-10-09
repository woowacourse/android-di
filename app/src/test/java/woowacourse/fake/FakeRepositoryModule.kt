package woowacourse.fake

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.DependencyModule

class FakeRepositoryModule : DependencyModule {
    val fakeProductRepository: CartRepository = FakeCartRepository()
}
