package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.di.DependencyContainer
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase

object AppDI {
    private lateinit var database: ShoppingDatabase

    lateinit var viewModelFactory: ReflectionViewModelFactory
        private set

    fun initialize(context: Context) {
        database =
            Room
                .databaseBuilder(
                    context.applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping.db",
                ).build()

        val container = DependencyContainer()
        val dao = database.cartProductDao()

        container.register(
            type = CartProductDao::class,
            instance = dao,
        )

        val roomRepository = container.resolve(DefaultCartRepository::class)
        val inMemoryRepository = InMemoryCartRepository()

        container.register(
            type = CartRepository::class,
            instance = roomRepository,
            qualifier = RoomCart::class,
        )

        container.register(
            type = CartRepository::class,
            instance = inMemoryRepository,
            qualifier = InMemoryCart::class,
        )

        viewModelFactory = ReflectionViewModelFactory(container)
    }
}
