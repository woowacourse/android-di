package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class AppContainer(
    private val applicationContext: Context,
) {
    private val database: ShoppingDatabase by lazy {
        ShoppingDatabase.getInstance(applicationContext)
    }

    val cartProductDao: CartProductDao by lazy {
        database.cartProductDao()
    }

    val productRepository: ProductRepository by lazy { DefaultProductRepository() }
    val cartRepository: CartRepository by lazy { DefaultCartRepository(cartProductDao) }
}
