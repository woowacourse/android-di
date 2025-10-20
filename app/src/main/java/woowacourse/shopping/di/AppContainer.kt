package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.di.ActivityContext
import woowacourse.di.ApplicationContext
import woowacourse.di.Container
import woowacourse.di.Database
import woowacourse.di.InMemory
import woowacourse.di.ScopeType
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.RoomCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.ui.cart.DateFormatter

object AppContainer : Container() {
    fun init(context: Context) {
        bindSingleton(
            type = Context::class,
            qualifier = ApplicationContext::class,
        ) { context.applicationContext }

        bindScoped(
            type = Context::class,
            qualifier = ActivityContext::class,
            scopeType = ScopeType.ACTIVITY,
        ) {
            val scopeContext = requireScopeContext()
            scopeContext.identifierOf<Context>(ScopeType.ACTIVITY)
        }

        bindSingleton(ShoppingDatabase::class) {
            Room
                .databaseBuilder(
                    get(Context::class, ApplicationContext::class),
                    ShoppingDatabase::class.java,
                    "shopping.db",
                ).build()
        }

        bindSingleton(CartProductDao::class) {
            get(ShoppingDatabase::class).cartProductDao()
        }

        bindScoped(
            type = CartRepository::class,
            qualifier = Database::class,
            scopeType = ScopeType.APPLICATION,
        ) {
            RoomCartRepository(get(CartProductDao::class))
        }

        bindScoped(
            type = CartRepository::class,
            qualifier = InMemory::class,
            scopeType = ScopeType.APPLICATION,
        ) {
            InMemoryCartRepository()
        }

        bindScoped(
            type = ProductRepository::class,
            scopeType = ScopeType.VIEW_MODEL,
        ) {
            DefaultProductRepository()
        }

        bindScoped(
            type = DateFormatter::class,
            scopeType = ScopeType.ACTIVITY,
        ) {
            DateFormatter(get(Context::class, ActivityContext::class))
        }
    }
}
