package woowacourse.shopping.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import woowacourse.shopping.data.entity.CartProductDao
import woowacourse.shopping.data.entity.CartProductEntity


@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao
}
