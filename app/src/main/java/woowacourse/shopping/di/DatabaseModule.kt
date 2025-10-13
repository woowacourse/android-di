package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.ShoppingDatabase

object DatabaseModule {
    fun database(context: Context): ShoppingDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ShoppingDatabase::class.java,
            "shopping_database",
        ).build()
    }

    fun cartProductDao(database: ShoppingDatabase) = database.cartProductDao()
}
