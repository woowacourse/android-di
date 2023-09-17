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

        @Synchronized
        fun getInstance(context: Context): ShoppingDatabase {
            return instance ?: synchronized(ShoppingDatabase::class) {
                Room.databaseBuilder(
                    context,
                    ShoppingDatabase::class.java,
                    "shopping-database",
                ).build().also { instance = it }
            }
        }
    }
}
