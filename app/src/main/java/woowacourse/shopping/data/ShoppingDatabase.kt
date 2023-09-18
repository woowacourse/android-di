package woowacourse.shopping.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CartProductEntity::class], version = 2, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao
}
