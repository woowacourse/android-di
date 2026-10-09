package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import com.cksckckcks.di.DiContainer
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCart
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.LocalMemoryCart
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.ui.cart.DateFormatter

class ShoppingContainer(
    context: Context,
) {
    val diContainer = DiContainer()

    private val database =
        Room
            .databaseBuilder(
                context,
                ShoppingDatabase::class.java,
                "shopping.db",
            ).build()

    init {
        val applicationContext = context.applicationContext
        diContainer.register(DateFormatter::class, scope = ShoppingScopes.screen) {
            DateFormatter(applicationContext)
        }

        diContainer.register(ProductRepository::class, scope = ShoppingScopes.viewModel) {
            ProductRepository()
        }

        diContainer.register(CartProductDao::class, scope = diContainer.applicationScope.type) {
            database.cartProductDao()
        }

        diContainer.register(CartRepository::class, LocalMemoryCart::class, scope = diContainer.applicationScope.type) {
            val dao = diContainer.getInstance(CartProductDao::class) as CartProductDao
            DefaultCartRepository(dao)
        }

        diContainer.register(CartRepository::class, InMemoryCart::class, scope = diContainer.applicationScope.type) {
            InMemoryCartRepository()
        }
    }
}
