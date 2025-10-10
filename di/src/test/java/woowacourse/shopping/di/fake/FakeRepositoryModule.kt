package woowacourse.shopping.di.fake

import woowacourse.shopping.core.DependencyModule
import woowacourse.shopping.di.fake.repository.FakeCartRepository
import woowacourse.shopping.domain.CartRepository

class FakeRepositoryModule : DependencyModule {
    val fakeProductRepository: CartRepository = FakeCartRepository()
}
