package woowacourse.shopping.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao

    companion object {
        private var instance: ShoppingDatabase? = null
        val instanceOrException get() = instance ?: throw IllegalArgumentException()

        fun init(context: Context): ShoppingDatabase? {
            if (instance == null) {
                synchronized(ShoppingDatabase::class) {
                    instance =
                        Room.databaseBuilder(
                            context.applicationContext,
                            ShoppingDatabase::class.java,
                            "shopping_database",
                        ).build()
                }
            }
            return instance
        }
    }
}
