package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository

class ShoppingApplication : Application() {
    companion object DependencyContainer {
        val productRepository = DefaultProductRepository()
        val cartRepository = DefaultCartRepository()
    }
}
