package woowacourse.shopping.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao

    companion object {
        @Volatile
        private lateinit var instance: ShoppingDatabase

        fun getInstance(context: Context): ShoppingDatabase {
            if (!::instance.isInitialized) initializeDatabase(context)
            return instance
        }

        private fun initializeDatabase(context: Context) {
            synchronized(this) {
                instance =
                    Room
                        .databaseBuilder(
                            context.applicationContext,
                            ShoppingDatabase::class.java,
                            "shopping.db",
                        )
                        .build()
            }
        }
    }
}
