package woowacourse.shopping.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao

    companion object {
        fun initialize(context: Context) =
            Room.databaseBuilder(
                context,
                ShoppingDatabase::class.java,
                "cart",
            ).build()
    }
}
