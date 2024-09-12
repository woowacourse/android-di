package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DataBaseCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.DependencyProvider
import woowacourse.shopping.di.Module

class ShoppingApplication : Application() {
    lateinit var repositoryModule: DependencyProvider
        private set

    override fun onCreate() {
        super.onCreate()
        val db = ShoppingDatabase.getInstance(this)

        repositoryModule =
            Module().apply {
                addDeferredTypes(
                    CartRepository::class to DataBaseCartRepository::class,
                    CartRepository::class to InMemoryCartRepository::class,
                    ProductRepository::class to ProductRepositoryImpl::class,
                )

                addInitializedInstances(
                    CartProductDao::class to db.cartProductDao(),
                )
            }
    }
}
