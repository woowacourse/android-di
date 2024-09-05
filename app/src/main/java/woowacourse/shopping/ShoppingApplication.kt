package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.di.Container

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Container.run {
            addInstance(ProductRepository::class, DefaultProductRepository())
            addInstance(CartRepository::class, DefaultCartRepository())
        }
    }
}
