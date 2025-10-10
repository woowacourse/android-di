package woowacourse.fake

import woowacourse.shopping.core.DependencyModule
import woowacourse.shopping.domain.CartRepository

class FakeDependencyModule : DependencyModule {
    val stringProvider: String = "inject"
    val cartRepository: CartRepository = FakeCartRepository()
}
