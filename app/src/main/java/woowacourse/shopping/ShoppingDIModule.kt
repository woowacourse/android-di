package woowacourse.shopping

import android.content.Context
import androidx.room.Room
import com.example.di.DIContainer
import com.example.di.annotation.Database
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

class ShoppingDIModule(
    private val context: Context,
) {
    fun register(container: DIContainer) {
        val database =
            Room
                .databaseBuilder(
                    context.applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping-database",
                ).build()

        container.register(CartProductDao::class) {
            println("[Module] ✅ CartProductDao 등록!")
            database.cartProductDao()
        }

        container.registerFactory(CartRepository::class, Database::class) { owner ->
            println("[Module] ✅ CartRepository(Database) → DefaultCartRepository 등록!")
            container.get(DefaultCartRepository::class, owner = owner)
        }

        container.registerFactory(ProductRepository::class) { owner ->
            println("[Module] ✅ ProductRepository(Database) → DefaultProductRepository등록 !")
            container.get(DefaultProductRepository::class, owner = owner)
        }
    }
}
