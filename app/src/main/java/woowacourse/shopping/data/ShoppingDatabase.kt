package woowacourse.shopping.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao

    companion object {
        const val DATABASE_NAME = "Shopping_Database"

        private var instance: ShoppingDatabase? = null

        fun instance(context: Context): ShoppingDatabase {
            if (instance == null) {
                synchronized(ShoppingDatabase::class) {
                    instance = instance ?: createDatabase(context)
                }
            }
            return instance ?: throw IllegalStateException("Database is not created")
        }

        private fun createDatabase(context: Context): ShoppingDatabase {
            return Room.databaseBuilder(
                context,
                ShoppingDatabase::class.java,
                DATABASE_NAME,
            ).build()
        }
    }
}
