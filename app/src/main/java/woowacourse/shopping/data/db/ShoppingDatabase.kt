package woowacourse.shopping.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao

    companion object {
        @Volatile
        private var Instance: ShoppingDatabase? = null

        fun getDatabase(context: Context): ShoppingDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ShoppingDatabase::class.java, "shopping.db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
