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

        fun getDataBase(context: Context): ShoppingDatabase =
            instance ?: synchronized(this) {
                val instance =
                    Room
                        .databaseBuilder(
                            context.applicationContext,
                            ShoppingDatabase::class.java,
                            "product_database",
                        ).fallbackToDestructiveMigration()
                        .build()
                Companion.instance = instance

                instance
            }
    }
}
