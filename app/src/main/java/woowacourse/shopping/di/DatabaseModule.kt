package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

object DatabaseModule {
    fun provideShoppingDatabase(context: Context): ShoppingDatabase = ShoppingDatabase.getInstance(context)

    fun provideCartProductDao(database: ShoppingDatabase): CartProductDao = database.cartProductDao()
}
