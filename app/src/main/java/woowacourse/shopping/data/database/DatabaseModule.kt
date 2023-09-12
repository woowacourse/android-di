package woowacourse.shopping.data.database

import android.content.Context
import androidx.room.Room

class DatabaseModule {

    fun providesShoppingDatabase(context: Context): ShoppingDatabase = Room.databaseBuilder(
        context,
        ShoppingDatabase::class.java,
        "ShoppingDatabase",
    ).build()
}
