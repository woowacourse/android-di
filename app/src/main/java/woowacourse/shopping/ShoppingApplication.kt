package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.KirbyDIContainer

class ShoppingApplication : Application() {
    lateinit var container: KirbyDIContainer
        private set

    override fun onCreate() {
        super.onCreate()
        val database =
            Room
                .databaseBuilder(this, ShoppingDatabase::class.java, "shopping.db")
                .build()
        container =
            KirbyDIContainer().apply {
                registerInstance(CartProductDao::class, database.cartProductDao())
                registerBinding(CartRepository::class, DefaultCartRepository::class)
            }
    }
}
