package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.data.local.CartProductDao
import woowacourse.shopping.data.local.ShoppingDatabase
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.di.DefaultDependencyContainer
import woowacourse.shopping.di.DependencyContainer
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.data.RepositoryQualifier.ROOM_DB
import woowacourse.shopping.data.RepositoryQualifier.IN_MEMORY


class ShoppingApplication : Application() {
    val database: ShoppingDatabase by lazy { ShoppingDatabase.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        defaultDependencyContainer.setDependency(
            ProductRepository::class,
            DefaultProductRepository::class,
            IN_MEMORY.name,
        )
        defaultDependencyContainer.setDependency(
            CartRepository::class,
            DefaultCartRepository::class,
            ROOM_DB.name,
        )
        defaultDependencyContainer.setDependency(
            CartProductDao::class,
            database.cartProductDao()::class,
        )
        defaultDependencyContainer.setInstance(
            CartProductDao::class,
            database.cartProductDao(),
        )
        DependencyInjector.initDependencyContainer(defaultDependencyContainer)
    }

    companion object {
        val defaultDependencyContainer: DependencyContainer by lazy {
            DefaultDependencyContainer()
        }
    }
}
