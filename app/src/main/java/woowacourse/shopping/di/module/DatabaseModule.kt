package woowacourse.shopping.di.module

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.DIContainer

class DatabaseModule(private val context: Context) : DIModule {
    override fun register(container: DIContainer) {
        val database =
            Room.databaseBuilder(
                context.applicationContext,
                ShoppingDatabase::class.java,
                "shopping_database",
            ).build()

        DIContainer.registerInstance(ShoppingDatabase::class, database)
        DIContainer.registerInstance(CartProductDao::class, database.cartProductDao())
    }
}
