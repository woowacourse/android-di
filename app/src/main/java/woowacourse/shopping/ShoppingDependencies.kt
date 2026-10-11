package woowacourse.shopping

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.annotation.InMemoryCart
import woowacourse.shopping.data.annotation.RoomCart
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.di.ScopeKey
import woowacourse.shopping.di.ScopeType
import woowacourse.shopping.ui.cart.DateFormatter

object AppScope : ScopeType

object ViewModelScope : ScopeType

object ScreenScope : ScopeType

val appScopeKey = ScopeKey(AppScope, "app")

fun registerShoppingDependencies(
    diContainer: DIContainer,
    application: Application,
) {
    val db =
        Room
            .databaseBuilder<ShoppingDatabase>(application, "shopping-database")
            .setDriver(AndroidSQLiteDriver())
            .build()

    diContainer.openScope(appScopeKey)

    diContainer.bindInstance(
        type = CartProductDao::class,
        instance = db.cartProductDao(),
        scopeKey = appScopeKey,
    )
    diContainer.bindInstance(
        type = Context::class,
        instance = application,
        scopeKey = appScopeKey,
    )
    diContainer.bind(
        type = CartRepository::class,
        implementation = DefaultCartRepository::class,
        qualifier = RoomCart::class,
        scopeType = AppScope,
    )
    diContainer.bind(
        type = CartRepository::class,
        implementation = InMemoryCartRepository::class,
        qualifier = InMemoryCart::class,
        scopeType = AppScope,
    )
    diContainer.bind(
        type = ProductRepository::class,
        implementation = ProductRepository::class,
        scopeType = ViewModelScope,
    )
    diContainer.bind(
        type = DateFormatter::class,
        implementation = DateFormatter::class,
        scopeType = ScreenScope,
    )
}
