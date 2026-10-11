package woowacourse.shopping

import android.app.Application
import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.AoDi
import woowacourse.shopping.di.InMemoryCart
import woowacourse.shopping.di.RoomCart
import woowacourse.shopping.di.ShoppingScopes
import woowacourse.shopping.ui.cart.DateFormatter

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 화면보다 먼저 실행되는 프로세스 초기화 지점에서 Room 객체를 한 번 준비한다.
        // ::class는 Kotlin의 KClass, .java는 Room이 요구하는 Java Class를 제공한다.
        val database =
            Room
                .databaseBuilder(
                    applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping.db",
                ).build()

        AoDi.openApplicationScope()
        AoDi.registerScopeRule(Context::class, ShoppingScopes.application)
        AoDi.registerScopeRule(CartProductDao::class, ShoppingScopes.application)
        AoDi.registerScopeRule(CartRepository::class, ShoppingScopes.application)
        AoDi.registerScopeRule(ProductRepository::class, ShoppingScopes.viewModel)
        AoDi.registerScopeRule(DateFormatter::class, ShoppingScopes.screen)

        // Room이 만들거나 Android가 제공하는 객체는 앱 스코프에 외부 객체로 등록한다.
        AoDi.register(Context::class, applicationContext, scopeContext = ShoppingScopes.applicationContext)
        AoDi.register(
            CartProductDao::class,
            database.cartProductDao(),
            scopeContext = ShoppingScopes.applicationContext,
        )
        AoDi.registerInterfaceRule(CartRepository::class, DefaultCartRepository::class, RoomCart::class)
        AoDi.registerInterfaceRule(CartRepository::class, InMemoryCartRepository::class, InMemoryCart::class)
    }
}
