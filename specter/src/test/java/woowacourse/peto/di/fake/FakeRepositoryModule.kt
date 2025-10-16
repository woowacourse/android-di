package woowacourse.peto.di.fake

import woowacourse.peto.di.DependencyModule
import woowacourse.peto.di.fake.repository.cart.CartRepository
import woowacourse.peto.di.fake.repository.cart.DefaultCartRepository

class FakeRepositoryModule : DependencyModule {
    val defaultCartRepository: CartRepository = DefaultCartRepository()
}
