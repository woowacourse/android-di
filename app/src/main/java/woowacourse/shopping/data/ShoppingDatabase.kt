package woowacourse.shopping.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import woowacourse.shopping.data.cart.CartProductDao
import woowacourse.shopping.data.cart.CartProductEntity

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao

    companion object {
        private const val databaseName = "ShoppingDatabase"
        private var instance: ShoppingDatabase? = null

        fun getDatabase(context: Context): ShoppingDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    ShoppingDatabase::class.java,
                    databaseName,
                ).build().let {
                    instance = it
                    it
                }
            }
        }
    }
}
