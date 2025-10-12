package woowacourse.shopping

import android.content.Context
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class AppContainer(
    context: Context,
) {
    val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl()
    }
    val cartRepository: CartRepository by lazy {
        CartRepositoryImpl(database.cartProductDao())
    }
    private val database: ShoppingDatabase by lazy {
        ShoppingDatabase.getInstance(context)
    }
}
