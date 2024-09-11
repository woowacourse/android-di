package woowacourse.shopping.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao

    companion object {
        private var instance: ShoppingDatabase? = null

        fun getInstance(context: Context): ShoppingDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping_database",
                ).build().also { instance = it }
            }
        }
    }
}
