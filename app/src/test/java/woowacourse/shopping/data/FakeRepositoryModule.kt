package woowacourse.shopping.data

import com.example.di.Dependency
import com.example.di.Module
import woowacourse.shopping.di.DatabaseRepository
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.fixture.FakeProductRepository

class FakeRepositoryModule : Module {
    @Dependency
    fun productRepository(): ProductRepository = FakeProductRepository()

    @Dependency
    @DatabaseRepository
    fun databaseCartRepository(dao: CartProductDao = FakeCartProductDao()): CartRepository = DatabaseCartRepository(dao)
}
