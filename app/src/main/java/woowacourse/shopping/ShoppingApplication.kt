package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

class ShoppingApplication : Application() {
    val diContainer: DIContainer by lazy { DIContainer() }

    override fun onCreate() {
        super.onCreate()
        diContainer.put(ProductRepository::class, DefaultProductRepository())
        diContainer.put(CartRepository::class, DefaultCartRepository())
    }
}
