package woowacourse.shopping

import android.app.Application
import woowacourse.di.InjectedComponent
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository

class ShoppingApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()

        container.injectedComponentContainer.add(
            InjectedComponent.InjectedSingletonComponent(ProductRepository::class, DefaultProductRepository()),
        )

        container.injectedComponentContainer.add(
            InjectedComponent.InjectedSingletonComponent(CartRepository::class, DefaultCartRepository()),
        )
    }
}
