package woowacourse.shopping.di

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.fixture.FakeCartProductDao
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.FakeProductRepository
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

object FakeModule : Module {
    fun provideProductRepository(): ProductRepository = FakeProductRepository

    fun provideCartRepository(cartProductDao: CartProductDao): CartRepository = FakeCartRepository.getInstance(cartProductDao)

    fun provideCartProductDao(): CartProductDao = FakeCartProductDao
}
