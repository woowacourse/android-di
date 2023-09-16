package woowacourse.shopping.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CartProductEntity::class], version = 1)
abstract class CartDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao
}
