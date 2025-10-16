package woowacourse.shopping.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao

    companion object {
        private const val DATABASE_NAME = "SHOPPING_DATABASE"

        @Suppress("ktlint:standard:property-naming")
        private var INSTANCE: ShoppingDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): ShoppingDatabase =
            INSTANCE ?: synchronized(LOCK) {
                Room
                    .databaseBuilder(
                        context.applicationContext,
                        ShoppingDatabase::class.java,
                        DATABASE_NAME,
                    ).build()
                    .also { INSTANCE = it }
            }
    }
}
