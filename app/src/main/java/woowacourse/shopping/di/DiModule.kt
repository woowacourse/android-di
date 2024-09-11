package woowacourse.shopping.di

import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

object DiModule : Module {
    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()

    fun provideCartRepository(cartProductDao: CartProductDao): CartRepository = CartRepositoryImpl(cartProductDao)

    fun provideCartProductDao(): CartProductDao = ShoppingApplication.appDatabase.cartProductDao()
}
