package woowacourse.shopping.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import woowacourse.shopping.data.entity.CartProductDao
import woowacourse.shopping.data.entity.CartProductEntity

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao

    companion object {
        fun from(context: Context) = Room.databaseBuilder(
            context,
            ShoppingDatabase::class.java,
            "ShoppingDatabase",
        ).build()
    }
}
