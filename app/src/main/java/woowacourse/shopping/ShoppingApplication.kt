package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import com.harodi.DiManager
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCart
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCart
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase

class ShoppingApplication : Application() {
    val diManager = DiManager()

    override fun onCreate() {
        super.onCreate()
        val database =
            Room
                .databaseBuilder(applicationContext, ShoppingDatabase::class.java, "shopping.db")
                .build()
        val cartProductDao = database.cartProductDao()
        diManager.addInstance(
            classType = ShoppingDatabase::class.java,
            qualifier = null,
            value = database,
        )
        diManager.addInstance(
            classType = CartProductDao::class.java,
            qualifier = null,
            value = cartProductDao,
        )

        diManager.addProvider(
            classType = CartRepository::class.java,
            qualifier = DefaultCart::class,
            value = DefaultCartRepository::class.java,
        )

        diManager.addProvider(
            classType = CartRepository::class.java,
            qualifier = InMemoryCart::class,
            value = InMemoryCartRepository::class.java,
        )
    }
}
