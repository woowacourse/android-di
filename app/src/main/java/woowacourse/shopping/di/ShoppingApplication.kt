package woowacourse.shopping.di

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ShoppingDatabase

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
            DependencyContainer().apply {
                registerInstance(ShoppingDatabase::class, database)
                registerFactory(CartProductDao::class) { container ->
                    container.get(ShoppingDatabase::class).cartProductDao()
                }
                registerFactory(CartRepository::class) { container ->
                    container.get(DefaultCartRepository::class)
                }
            }

        viewModelFactory = AutoViewModelFactory(dependencyContainer)
    }
}
