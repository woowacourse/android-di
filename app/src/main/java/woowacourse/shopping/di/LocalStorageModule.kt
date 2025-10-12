package woowacourse.shopping.di

import woowacourse.shopping.data.ShoppingDatabase

object LocalStorageModule {
    private val shoppingDatabase by lazy {
        ShoppingDatabase.getDataBase(ApplicationContextProvider.applicationContext)
    }
    val cartProductDao by lazy { shoppingDatabase.cartProductDao() }
}
