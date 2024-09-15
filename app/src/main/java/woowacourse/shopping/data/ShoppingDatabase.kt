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
        private var instance: ShoppingDatabase? = null

        fun getInstance(context: Context): ShoppingDatabase {
            if (instance == null) initializeDatabase(context)
            return instance
                ?: throw IllegalStateException(EXCEPTION_UNINITIALIZED_DATABASE_ACCESS)
        }

        private fun initializeDatabase(context: Context) {
            synchronized(this) {
                instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        ShoppingDatabase::class.java,
                        "shopping.db",
                    )
                    .build()
            }
        }

        private const val EXCEPTION_UNINITIALIZED_DATABASE_ACCESS =
            "Database has not been initialized."
    }
}
