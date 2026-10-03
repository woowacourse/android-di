package woowacourse.shopping.di

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.RoomCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.di.dependencyContainer as buildDependencyContainer

class ShoppingApplication : Application() {
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

        val dependencyContainer =
            buildDependencyContainer {
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
            }

        viewModelFactory = AutoViewModelFactory(dependencyContainer)
    }
}
