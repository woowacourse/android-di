package woowacourse.shopping

import android.app.Application
import com.woowacourse.di.ActivityScope
import com.woowacourse.di.DependencyInjector
import com.woowacourse.di.InMemory
import com.woowacourse.di.RoomDB
import com.woowacourse.di.Singleton
import com.woowacourse.di.ViewModelScope
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository
import woowacourse.shopping.ui.cart.DateFormatter

class ShoppingApplication : Application() {
    private val db by lazy { ShoppingDatabase.initialize(applicationContext) }
    private val cartProductDao by lazy { db.cartProductDao() }

    override fun onCreate() {
        super.onCreate()
        dependencyInjector = DependencyInjector()
        initialize()
    }

    private fun initialize() {
        registerDateFormatter()
        registerProductRepository()
        registerCartRepository()
    }

    private fun registerDateFormatter() {
        dependencyInjector.addInstance(
            DateFormatter::class,
            RepositoryModule.provideDateFormatter(applicationContext),
            scope = ActivityScope::class,
        )
    }

    private fun registerProductRepository() {
        dependencyInjector.addInstance(
            ProductRepository::class,
            RepositoryModule.provideProductRepository(),
            qualifier = InMemory::class,
            scope = ViewModelScope::class,
        )
    }

    private fun registerCartRepository() {
        dependencyInjector.addInstance(
            CartRepository::class,
            RepositoryModule.provideCartRepository(cartProductDao),
            qualifier = RoomDB::class,
            scope = Singleton::class,
        )
        dependencyInjector.addInstance(
            CartRepository::class,
            RepositoryModule.provideCartInMemoryRepository(),
            qualifier = InMemory::class,
            scope = Singleton::class,
        )
    }

    companion object {
        lateinit var dependencyInjector: DependencyInjector
            private set
    }
}
