package woowacourse.shopping.ui

import woowacourse.shopping.data.InMemory
import woowacourse.shopping.data.RoomDB
import woowacourse.shopping.data.local.CartProductDao
import woowacourse.shopping.data.local.ShoppingDatabase
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.di.DependencyInjectedApplication
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class ShoppingApplication : DependencyInjectedApplication() {
    private val database: ShoppingDatabase by lazy { ShoppingDatabase.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
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
    }

    companion object {
        private var instance: ShoppingApplication? = null

        fun getApplication(): ShoppingApplication {
            if (instance == null) {
                instance = ShoppingApplication()
            }
            return instance!!
        }
    }
}
