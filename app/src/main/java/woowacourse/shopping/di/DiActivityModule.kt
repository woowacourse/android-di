package woowacourse.shopping.di

import woowacourse.shopping.data.CartInDiskRepository
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.repository.CartRepository

class DiActivityModule(parentDiContainer: DiContainer) : DiContainer(parentDiContainer) {
    fun provideCartInDiskRepository(
        cartProductDao: CartProductDao,
    ): CartRepository = CartInDiskRepository(cartProductDao)

    fun provideCartProductDao(
        shoppingDatabase: ShoppingDatabase,
    ): CartProductDao = shoppingDatabase.cartProductDao()
}
