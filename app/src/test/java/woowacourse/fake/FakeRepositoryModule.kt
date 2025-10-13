package woowacourse.fake

import woowacourse.peto.di.DependencyModule
import woowacourse.peto.di.annotation.Qualifier
import woowacourse.shopping.data.db.CartProductDao
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class FakeRepositoryModule(
    private val cartDao: CartProductDao,
) : DependencyModule {
    @Qualifier("default")
    val fakeRepository: CartRepository by lazy { FakeCartRepository(cartDao) }
    val fakeProductRepository: ProductRepository = FakeProductRepository()
}
