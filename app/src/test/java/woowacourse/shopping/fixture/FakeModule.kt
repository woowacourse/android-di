package woowacourse.shopping.fixture

import com.example.seogi.di.Module
import com.example.seogi.di.annotation.InMemory
import com.example.seogi.di.annotation.OnDisk
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

object FakeModule : Module {
    fun provideProductRepository(): ProductRepository = FakeProductRepository

    @OnDisk
    fun provideCartRepositoryOnDisk(cartProductDao: CartProductDao): CartRepository = FakeCartRepositoryOnDisk.getInstance(cartProductDao)

    @InMemory
    fun provideCartRepositoryInMemory(): CartRepository = FakeCartRepositoryInMemory.getInstance()

    fun provideCartProductDao(): CartProductDao = FakeCartProductDao
}
