package woowacourse.shopping.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import woowacourse.di.DependencyContainer
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.RoomCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.di.dependencyContainer as buildDependencyContainer

class ShoppingApplication : Application() {
    lateinit var dependencyContainer: DependencyContainer
        private set

    lateinit var viewModelFactory: AutoViewModelFactory
        private set

    override fun onCreate() {
        super.onCreate()

        val databaseBuilder =
            Room.databaseBuilder(
                applicationContext,
                ShoppingDatabase::class.java,
                "shopping.db",
            )
        val database = databaseBuilder.build()

        dependencyContainer =
            buildDependencyContainer {
                registerInstance(Context::class, applicationContext)
                registerInstance(ShoppingDatabase::class, database)
                registerFactory(CartProductDao::class) { container ->
                    container.get(ShoppingDatabase::class).cartProductDao()
                }
                registerFactory(CartRepository::class, RoomCart::class) { container ->
                    container.get(RoomCartRepository::class)
                }
                registerFactory(CartRepository::class, InMemoryCart::class) { container ->
                    container.get(InMemoryCartRepository::class)
                }
                registerFactory(ProductRepository::class, scope = ShoppingScopes.VIEW_MODEL) {
                    ProductRepository()
                }
                registerFactory(DateFormatter::class, scope = ShoppingScopes.NAVIGATION_DESTINATION) { container ->
                    DateFormatter(container.get(Context::class))
                }
            }

        viewModelFactory = AutoViewModelFactory(dependencyContainer)
    }
}
