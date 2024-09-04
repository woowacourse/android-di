package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

class ShoppingApplication : Application() {
    val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl()
    }

    val cartRepository: CartRepository by lazy {
        CartRepositoryImpl()
    }
}
