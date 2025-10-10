package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DependencyInjectorImpl
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class ShoppingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DependencyInjectorImpl.setInstance(CartRepository::class, AppContainer.cartRepository)
        DependencyInjectorImpl.setInstance(ProductRepository::class, AppContainer.productRepository)
    }
}
