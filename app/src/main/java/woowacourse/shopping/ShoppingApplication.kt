package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.Injector

class ShoppingApplication : Application() {
    val injector: Injector by lazy {
        Injector().apply {
            registerSingleton(ShoppingDatabase::class) {
                Room
                    .databaseBuilder(
                        this@ShoppingApplication,
                        ShoppingDatabase::class.java,
                        "shopping.db",
                    ).build()
            }
            registerSingleton(CartProductDao::class) {
                create(ShoppingDatabase::class).cartProductDao()
            }
            registerSingleton(CartRepository::class) {
                create(DefaultCartRepository::class)
            }
        }
    }
}
