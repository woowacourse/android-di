package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.ShoppingDatabase

object DatabaseModule {

    @Volatile
    private var INSTANCE: ShoppingDatabase? = null

    fun cartProductDao(context: Context) = database(context).cartProductDao()

    private fun database(context: Context): ShoppingDatabase =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
        }

    private fun buildDatabase(appContext: Context): ShoppingDatabase =
        Room.databaseBuilder(
            appContext,
            ShoppingDatabase::class.java,
            "shopping_database",
        )
            // .fallbackToDestructiveMigration() // 필요 시
            .build()
}
