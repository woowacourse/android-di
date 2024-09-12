package woowacourse.shopping.di.module

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.di.annotation.DatabaseRepository

class DatabaseModule(private val context: Context) : DIModule {
    override fun register(container: DIContainer) {
        val database =
            Room.databaseBuilder(
                context.applicationContext,
                ShoppingDatabase::class.java,
                "shopping_database",
            ).build()

        val cartProductDao = database.cartProductDao()

        // CartProductDao를 DatabaseRepository로 등록
        container.registerInstance(CartProductDao::class, cartProductDao, DatabaseRepository::class)

        // ShoppingDatabase를 DatabaseRepository로 등록
        container.registerInstance(ShoppingDatabase::class, database, DatabaseRepository::class)
    }
}
