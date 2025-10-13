package woowacourse.shopping

import android.content.Context
import androidx.room.Room
import com.example.di.AppContainer
import com.example.di.DIModule
import com.example.di.annotation.Database
import com.example.di.annotation.InMemory
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

class ShoppingDIModule(
    private val context: Context,
) : DIModule {
    override fun register(container: AppContainer) {
        val database =
            Room
                .databaseBuilder(
                    context.applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping-database",
                ).build()

        container.register(CartProductDao::class, database.cartProductDao())

        container.bind(CartRepository::class, DefaultCartRepository::class, null)
        container.bind(ProductRepository::class, DefaultProductRepository::class, null)

        container.bind(CartRepository::class, DefaultCartRepository::class, Database::class)
        container.bind(ProductRepository::class, DefaultProductRepository::class, Database::class)

        // InMemory 버전 작성 (임시)
        container.bind(CartRepository::class, DefaultCartRepository::class, InMemory::class)
        container.bind(ProductRepository::class, DefaultProductRepository::class, InMemory::class)
    }
}
