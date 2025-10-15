package woowacourse.shopping.di

import android.content.Context
import com.on.di_library.di.annotation.MyModule
import com.on.di_library.di.annotation.MyProvider
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

@MyModule
object LocalStorageModule {
    @MyProvider
    fun shoppingDatabase(context: Context) = ShoppingDatabase.getDataBase(context)

    @MyProvider
    fun cartProductDao(database: ShoppingDatabase): CartProductDao = database.cartProductDao()
}
