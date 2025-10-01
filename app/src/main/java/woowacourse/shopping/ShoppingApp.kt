package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DiContainer
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class ShoppingApp : Application() {
    private val appContainer = AppContainer()

    override fun onCreate() {
        super.onCreate()
        DiContainer.setInstance(CartRepository::class, appContainer.cartRepository)
        DiContainer.setInstance(ProductRepository::class, appContainer.productRepository)
    }
}
