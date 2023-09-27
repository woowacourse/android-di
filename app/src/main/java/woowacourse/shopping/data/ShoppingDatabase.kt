package woowacourse.shopping.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import woowacourse.shopping.annotation.ApplicationLifecycle

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao

    companion object {
        private const val name = "SHOPPING_DATABASE"
        private var instance: ShoppingDatabase? = null

        @Synchronized
        fun getInstance(@ApplicationLifecycle context: Context): ShoppingDatabase {
            return instance ?: synchronized(ShoppingDatabase::class) {
                createInstance(context)
            }
        }

        private fun createInstance(context: Context): ShoppingDatabase {
            return Room.databaseBuilder(
                context,
                ShoppingDatabase::class.java,
                name,
            )
                .build()
                .also { instance = it }
        }
    }
}
