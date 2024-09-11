package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.data.local.CartProductDao
import woowacourse.shopping.data.local.ShoppingDatabase
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.ui.util.DependencyContainer
import woowacourse.shopping.ui.util.DependencyInjector
import woowacourse.shopping.ui.util.DefaultDependencyContainer

class ShoppingApplication : Application() {
    val database: ShoppingDatabase by lazy { ShoppingDatabase.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        defaultDependencyContainer.setDependency(
            ProductRepository::class,
            DefaultProductRepository::class,
        )
        defaultDependencyContainer.setDependency(
            CartRepository::class,
            DefaultCartRepository::class,
        )
        defaultDependencyContainer.setDependency(
            CartProductDao::class,
            database.cartProductDao()::class,
        )
        defaultDependencyContainer.setInstance(
            CartProductDao::class,
            database.cartProductDao()
        )
        DependencyInjector.initDependencyContainer(defaultDependencyContainer)
    }

    companion object {
        val defaultDependencyContainer: DependencyContainer by lazy {
            DefaultDependencyContainer()
        }
    }
}
