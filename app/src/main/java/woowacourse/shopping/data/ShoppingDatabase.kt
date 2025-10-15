package woowacourse.shopping.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.jvm.java

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao

    companion object {
        private var instance: ShoppingDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ShoppingDatabase {
            return this.instance ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        ShoppingDatabase::class.java,
                        "shopping-database",
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                this.instance = instance
                instance
            }
        }

        @Synchronized
        fun getInMemoryInstance(context: Context): ShoppingDatabase {
            return Room.inMemoryDatabaseBuilder(
                context.applicationContext,
                ShoppingDatabase::class.java,
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
