package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.di.DependencyContainer
import woowacourse.di.ScopeType
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase

object AppDI {
    internal val appScopeType = ScopeType("app")
    internal val viewModelScopeType = ScopeType("viewModel")

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

        val dao = database.cartProductDao()
        val container = createContainer(dao)
        val appScope = container.openScope(appScopeType, "application")
        viewModelFactory = ReflectionViewModelFactory(container, appScope, viewModelScopeType)
    }

    internal fun createContainer(dao: CartProductDao): DependencyContainer {
        val container = DependencyContainer()

        container.bind(
            type = CartProductDao::class,
            scopeType = appScopeType,
        ) { dao }

        container.bind(
            type = CartRepository::class,
            scopeType = appScopeType,
            qualifier = RoomCart::class,
        ) { DefaultCartRepository(dao) }

        container.bind(
            type = CartRepository::class,
            scopeType = appScopeType,
            qualifier = InMemoryCart::class,
        ) { InMemoryCartRepository() }

        container.bind(ProductRepository::class, viewModelScopeType)

        return container
    }
}
