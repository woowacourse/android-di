package woowacourse.shopping.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao

    companion object {
        private const val CART_PRODUCT_DATABASE = "CART_PRODUCT_DATABASE"
        private var shoppingDatabase: ShoppingDatabase? = null

        fun getShoppingDatabase(
            context: Context,
        ): ShoppingDatabase {
            synchronized(this) {
                return shoppingDatabase ?: run {
                    Room.databaseBuilder(
                        context,
                        ShoppingDatabase::class.java,
                        CART_PRODUCT_DATABASE,
                    ).build().also {
                        shoppingDatabase = it
                    }
                }
            }
        }
    }
}
