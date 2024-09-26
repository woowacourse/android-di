package woowacourse.shopping.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import woowacourse.shopping.local.dao.CartProductDao
import woowacourse.shopping.local.entity.CartProductEntity

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao
}
