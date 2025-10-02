package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.RepositoryModule
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class ShoppingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContainer.addProviders(CartRepository::class, RepositoryModule.cartRepository)
        AppContainer.addProviders(ProductRepository::class, RepositoryModule.productRepository)
    }
}