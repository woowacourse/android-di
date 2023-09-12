package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.database.ShoppingDatabase

object DatabaseModule {

    fun providesShoppingDatabase(context: Context): ShoppingDatabase = Room.databaseBuilder(
        context,
        ShoppingDatabase::class.java,
        "ShoppingDatabase",
    ).build()
}
