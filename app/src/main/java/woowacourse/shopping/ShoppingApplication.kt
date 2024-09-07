package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.di.DependencyProvider
import woowacourse.shopping.di.RepositoryModule

class ShoppingApplication : Application() {
    lateinit var repositoryModule: DependencyProvider
        private set

    override fun onCreate() {
        super.onCreate()
        repositoryModule =
            RepositoryModule(
                CartRepository::class to CartRepositoryImpl::class,
                ProductRepository::class to ProductRepositoryImpl::class,
            )
    }
}
