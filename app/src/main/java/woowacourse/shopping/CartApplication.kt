package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DiContainer
import woowacourse.shopping.di.RepositoryModule
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

class CartApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        postInstances()
    }

    private fun postInstances() {
        DiContainer.postInstance(CartRepository::class, RepositoryModule.cartRepository)
        DiContainer.postInstance(ProductRepository::class, RepositoryModule.productRepository)
    }
}
