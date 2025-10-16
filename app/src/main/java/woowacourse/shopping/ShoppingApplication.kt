package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import kotlin.jvm.java

class ShoppingApplication : Application() {
    val container: ShoppingContainer by lazy {
        val container = ShoppingContainer()

        container.register(ShoppingDatabase::class) {
            Room.databaseBuilder(
                applicationContext,
                ShoppingDatabase::class.java,
                "shopping.db",
            ).build()
        }

        container.register(CartProductDao::class) {
            container.get(ShoppingDatabase::class).cartProductDao()
        }

        container.register(ProductRepository::class) { DefaultProductRepository() }
        container.register(CartRepository::class) {
            DefaultCartRepository(
                container.get(
                    CartProductDao::class,
                ),
            )
        }

        container
    }
}
