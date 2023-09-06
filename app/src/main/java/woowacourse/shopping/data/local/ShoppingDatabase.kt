package woowacourse.shopping.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao

    companion object {
        private const val DB_NAME = "shopping_database"

        @Volatile
        private var INSTANCE: ShoppingDatabase? = null
        private fun createInstance(context: Context): ShoppingDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ShoppingDatabase::class.java,
                DB_NAME,
            ).build()
        }

        fun getDatabase(context: Context): ShoppingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = createInstance(context)
                INSTANCE = instance
                return instance
            }
        }
    }
}
