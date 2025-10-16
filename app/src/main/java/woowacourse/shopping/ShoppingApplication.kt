package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import com.example.di.ShoppingContainer
import com.example.domain.repository.CartRepository
import com.example.domain.repository.ProductRepository
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultInMemoryCartRepository
import woowacourse.shopping.data.DefaultRoomCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase

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

        container.register(type = CartRepository::class, qualifier = "room") {
            DefaultRoomCartRepository(
                container.get(
                    CartProductDao::class,
                ),
            )
        }

        container.register(
            type = CartRepository::class,
            qualifier = "in_memory"
        ) { DefaultInMemoryCartRepository() }

        container
    }
}