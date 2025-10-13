package woowacourse.shopping.di

import com.on.di_library.di.annotation.MyModule
import com.on.di_library.di.annotation.MyProvider
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

@MyModule
object LocalStorageModule {
    private val shoppingDatabase by lazy {
        ShoppingDatabase.getDataBase(ApplicationContextProvider.applicationContext)
    }

    @MyProvider
    fun cartProductDao(): CartProductDao = shoppingDatabase.cartProductDao()
}
