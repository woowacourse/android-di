package woowacourse.shopping.di.fake

import woowacourse.shopping.core.DependencyModule
import woowacourse.shopping.di.fake.repository.cart.CartRepository
import woowacourse.shopping.di.fake.repository.cart.DefaultCartRepository

class FakeRepositoryModule : DependencyModule {
    val defaultCartRepository: CartRepository = DefaultCartRepository()
}
