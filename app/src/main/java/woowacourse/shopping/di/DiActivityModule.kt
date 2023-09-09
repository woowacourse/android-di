package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartInDiskRepository
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.ui.cart.DateFormatter

class DiActivityModule(
    parentDiContainer: DiContainer,
    private val context: Context,
) : DiContainer(parentDiContainer) {
    fun provideContext(): Context = Cache.context.get {
        context
    }

    fun provideCartInDiskRepository(
        cartProductDao: CartProductDao,
    ): CartRepository = Cache.cartInDiskRepository.get {
        CartInDiskRepository(cartProductDao)
    }

    fun provideCartProductDao(
        shoppingDatabase: ShoppingDatabase,
    ): CartProductDao = Cache.cartProductDao.get {
        shoppingDatabase.cartProductDao()
    }

    fun provideDateFormatter(
        context: Context,
    ): DateFormatter = Cache.dataFormatter.get {
        DateFormatter(context)
    }

    private object Cache {
        val cartInDiskRepository = InstanceHolder<CartRepository>()
        val cartProductDao = InstanceHolder<CartProductDao>()
        val dataFormatter = InstanceHolder<DateFormatter>()
        val context = InstanceHolder<Context>()
    }
}
