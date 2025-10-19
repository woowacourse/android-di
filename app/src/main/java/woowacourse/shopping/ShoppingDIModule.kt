package woowacourse.shopping

import android.content.Context
import androidx.room.Room
import com.example.di.DIContainer
import com.example.di.DIModule
import com.example.di.annotation.Database
import com.example.di.annotation.InMemory
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

class ShoppingDIModule(
    private val context: Context,
) : DIModule {
    override fun register(container: DIContainer) {
        val database =
            Room
                .databaseBuilder(
                    context.applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping-database",
                ).build()

        container.register(CartProductDao::class) { database.cartProductDao() }

        // 기본 (Qualifier 없음 = null) 바인딩
        container.register(
            CartRepository::class,
            null,
        ) { container.resolve(DefaultCartRepository::class) }
        container.register(ProductRepository::class, null) {
            container.resolve(
                DefaultProductRepository::class,
            )
        }

        // @Database Qualifier 바인딩
        container.register(CartRepository::class, Database::class) {
            container.resolve(
                DefaultCartRepository::class,
            )
        }
        container.register(ProductRepository::class, Database::class) {
            container.resolve(
                DefaultProductRepository::class,
            )
        }

        // @InMemory Qualifier 바인딩
        container.register(CartRepository::class, InMemory::class) {
            container.resolve(
                InMemoryCartRepository::class,
            )
        }
        container.register(ProductRepository::class, InMemory::class) {
            container.resolve(
                DefaultProductRepository::class,
            )
        }
    }
}
