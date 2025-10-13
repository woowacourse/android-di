package woowacourse.shopping.di

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.FakeProductRepository

class FakeAppContainer : Any() {
    val productRepository: ProductRepository = FakeProductRepository()
    val cartRepository: CartRepository = FakeCartRepository()
}
