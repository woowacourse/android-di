package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import com.harodi.DiManager
import com.harodi.ScopeKey
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repositoryImpl.DefaultCart
import woowacourse.shopping.data.repositoryImpl.DefaultCartRepository
import woowacourse.shopping.data.repositoryImpl.InMemoryCart
import woowacourse.shopping.data.repositoryImpl.InMemoryCartRepository
import woowacourse.shopping.data.repositoryImpl.ProductRepository
import woowacourse.shopping.di.Scope

class ShoppingApplication : Application() {
    val diManager = DiManager()
    val applicationKey =
        ScopeKey(
            parentKey = null,
            scopeKind = Scope.APPLICATION,
        )

    override fun onCreate() {
        super.onCreate()
        val database =
            Room
                .databaseBuilder(applicationContext, ShoppingDatabase::class.java, "shopping.db")
                .build()
        val cartProductDao = database.cartProductDao()
        diManager.addScopePolicy(
            classType = ShoppingDatabase::class.java,
            qualifier = null,
            scopeKind = Scope.APPLICATION,
        )
        diManager.addScopePolicy(
            classType = CartProductDao::class.java,
            qualifier = null,
            scopeKind = Scope.APPLICATION,
        )
        diManager.addScopePolicy(
            classType = CartRepository::class.java,
            qualifier = DefaultCart::class,
            scopeKind = Scope.APPLICATION,
        )
        diManager.addScopePolicy(
            classType = CartRepository::class.java,
            qualifier = InMemoryCart::class,
            scopeKind = Scope.APPLICATION,
        )
        diManager.addScopePolicy(
            classType = ProductRepository::class.java,
            qualifier = null,
            scopeKind = Scope.VIEW_MODEL,
        )

        diManager.addScope(
            scopeKey = applicationKey,
            classType = ShoppingDatabase::class.java,
            qualifier = null,
            value = database,
        )
        diManager.addScope(
            scopeKey = applicationKey,
            classType = CartProductDao::class.java,
            qualifier = null,
            value = cartProductDao,
        )

        diManager.addProvider(
            classType = CartRepository::class.java,
            qualifier = DefaultCart::class,
            value = DefaultCartRepository::class.java,
        )

        diManager.addProvider(
            classType = CartRepository::class.java,
            qualifier = InMemoryCart::class,
            value = InMemoryCartRepository::class.java,
        )
    }
}
