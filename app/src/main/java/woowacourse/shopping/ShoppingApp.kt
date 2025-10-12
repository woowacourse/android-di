package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class ShoppingApp : Application() {
    private val appContainer by lazy { AppContainer(this) }

    override fun onCreate() {
        super.onCreate()
        DependencyInjector.setInstance(ProductRepository::class, appContainer.productRepository)
        DependencyInjector.setInstance(
            CartRepository::class,
            appContainer.cartRepository,
        )
    }
}
