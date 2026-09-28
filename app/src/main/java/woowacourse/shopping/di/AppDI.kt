package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
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

        container.register(CartProductDao::class, dao)

        val repository = container.resolve(DefaultCartRepository::class)
        container.register(CartRepository::class, repository)

        viewModelFactory = ReflectionViewModelFactory(container)
    }
}
