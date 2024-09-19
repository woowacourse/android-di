package woowacourse.shopping.di

import com.example.seogi.di.Module
import com.example.seogi.di.annotation.Qualifier
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepositoryInMemory
import woowacourse.shopping.data.CartRepositoryOnDisk
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

@Qualifier
annotation class OnDisk

@Qualifier
annotation class InMemory

object DiModule : Module {
    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()

    @InMemory
    fun provideCartRepositoryInMemory(): CartRepository = CartRepositoryInMemory()

    @OnDisk
    fun provideCartRepositoryOnDisk(cartProductDao: CartProductDao): CartRepository = CartRepositoryOnDisk(cartProductDao)

    fun provideCartProductDao(): CartProductDao = ShoppingApplication.appDatabase.cartProductDao()
}
