package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.Room
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.annotation.Provides

class DefaultDependencies(context: Context) : Dependencies {

    private val shoppingDatabase: ShoppingDatabase by lazy { ShoppingDatabase.getDatabase(context) }

    @Provides
    @Room
    val cartProductDao: CartProductDao by lazy { shoppingDatabase.cartProductDao() }
}
