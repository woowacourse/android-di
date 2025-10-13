package woowacourse.shopping.di

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.annotation.MyModule
import woowacourse.shopping.di.annotation.MyProvider

@MyModule
object LocalStorageModule {
    private val shoppingDatabase by lazy {
        ShoppingDatabase.getDataBase(ApplicationContextProvider.applicationContext)
    }

    @MyProvider
    fun cartProductDao(): CartProductDao = shoppingDatabase.cartProductDao()
}
