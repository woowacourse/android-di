package woowacourse.shopping.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao

    companion object {
        private const val DATABASE_NAME = "cart.db"

        @Volatile
        private var instance: ShoppingDatabase? = null

        fun instance(context: Context): ShoppingDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context,
                    ShoppingDatabase::class.java,
                    DATABASE_NAME,
                ).build().also { instance = it }
            }
    }
}
