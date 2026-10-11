package woowacourse.shopping.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import woowacourse.di.DependencyContainer
import woowacourse.di.DependencyKey
import woowacourse.di.DependencyScope
import woowacourse.di.ScopeType
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.ui.cart.DateFormatter

object AppDI {
    private val applicationScopeType = ScopeType("application")
    private val viewModelScopeType = ScopeType("viewModel")
    private val screenScopeType = ScopeType("screen")

    private val container =
        DependencyContainer(
            bindings =
                mapOf(
                    DependencyKey(CartRepository::class, RoomCart::class) to DefaultCartRepository::class,
                    DependencyKey(CartRepository::class, InMemoryCart::class) to InMemoryCartRepository::class,
                ),
            scopes =
                mapOf(
                    DependencyKey(CartRepository::class, RoomCart::class) to applicationScopeType,
                    DependencyKey(CartRepository::class, InMemoryCart::class) to applicationScopeType,
                    DependencyKey(ProductRepository::class) to viewModelScopeType,
                    DependencyKey(DateFormatter::class) to screenScopeType,
                ),
        )
    private lateinit var applicationScope: DependencyScope

    fun initialize(application: Application) {
        if (::applicationScope.isInitialized) return
        val scope = container.openScope(applicationScopeType)
        scope.register(Context::class, application.applicationContext)
        val database = Room.databaseBuilder(application, ShoppingDatabase::class.java, "shopping.db").build()
        scope.register(CartProductDao::class, database.cartProductDao())
        applicationScope = scope
    }

    fun openScreenScope(): DependencyScope = applicationScope.openChild(screenScopeType)

    fun viewModelFactory(parent: DependencyScope = applicationScope): ViewModelProvider.Factory =
        ReflectionViewModelFactory(parent, viewModelScopeType)
}
