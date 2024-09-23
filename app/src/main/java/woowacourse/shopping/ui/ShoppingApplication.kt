package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.data.InMemory
import woowacourse.shopping.data.RoomDB
import woowacourse.shopping.data.local.CartProductDao
import woowacourse.shopping.data.local.ShoppingDatabase
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.di.ApplicationDependencyContainer
import woowacourse.shopping.di.LifecycleAwareDependencyInjector
import woowacourse.shopping.di.LifecycleDependencyContainer
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class ShoppingApplication : Application() {
    val database: ShoppingDatabase by lazy { ShoppingDatabase.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        applicationDependencyContainer.setLifecycle(this)
        applicationDependencyContainer.setDependency(
            ProductRepository::class,
            DefaultProductRepository::class,
            InMemory::class,
        )
        applicationDependencyContainer.setDependency(
            CartRepository::class,
            DefaultCartRepository::class,
            RoomDB::class,
        )
        applicationDependencyContainer.setDependency(
            CartProductDao::class,
            database.cartProductDao()::class,
        )
        applicationDependencyContainer.setInstance(
            CartProductDao::class,
            database.cartProductDao(),
        )
        LifecycleAwareDependencyInjector().initDependencyContainer(applicationDependencyContainer)
    }

    companion object {
        val applicationDependencyContainer: LifecycleDependencyContainer by lazy {
            ApplicationDependencyContainer()
        }
    }
}
