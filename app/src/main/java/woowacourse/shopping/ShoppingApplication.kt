package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository

class ShoppingApplication : Application() {
    val container: ShoppingContainer by lazy {
        val container = ShoppingContainer()
        container.make(ProductRepository::class) { DefaultProductRepository() }
        container.make(CartRepository::class) { DefaultCartRepository() }
        container
    }
}
