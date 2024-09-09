package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.di.DependencyContainer

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DependencyContainer.run {
            addInstance(ProductRepository::class, DefaultProductRepository())
            addInstance(CartRepository::class, DefaultCartRepository())
        }
    }
}
