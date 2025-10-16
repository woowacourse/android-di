package woowacourse.shopping.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao

    companion object {
        private const val DATABASE_NAME = "shopping_database"

        @Volatile
        private var instance: ShoppingDatabase? = null

        fun instance(context: Context): ShoppingDatabase =
            instance ?: synchronized(this) {
                val newInstance: ShoppingDatabase =
                    Room
                        .databaseBuilder(
                            context.applicationContext,
                            ShoppingDatabase::class.java,
                            DATABASE_NAME,
                        ).build()
                newInstance.also { newInstance: ShoppingDatabase -> instance = newInstance }
            }
    }
}
