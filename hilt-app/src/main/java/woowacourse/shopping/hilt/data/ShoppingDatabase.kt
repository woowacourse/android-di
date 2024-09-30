package woowacourse.shopping.hilt.data

import androidx.room.Database
import androidx.room.RoomDatabase
import woowacourse.shopping.hilt.data.CartProductDao
import woowacourse.shopping.hilt.data.CartProductEntity

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao

    companion object {
        const val NAME = "shopping.db"
    }
}
