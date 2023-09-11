package woowacourse.shopping.data

import androidx.room.Database
import androidx.room.RoomDatabase
import woowacourse.shopping.data.cart.CartProductDao
import woowacourse.shopping.data.cart.CartProductEntity

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao
}
