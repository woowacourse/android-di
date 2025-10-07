package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DiContainer
import woowacourse.shopping.di.RepositoryModule
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class ShoppingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DiContainer.addProviders(CartRepository::class) { RepositoryModule.cartRepository }
        DiContainer.addProviders(ProductRepository::class) { RepositoryModule.productRepository }
    }
}
