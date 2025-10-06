package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DiContainer
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class ShoppingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DiContainer.setInstance(CartRepository::class, AppContainer.cartRepository)
        DiContainer.setInstance(ProductRepository::class, AppContainer.productRepository)
    }
}
