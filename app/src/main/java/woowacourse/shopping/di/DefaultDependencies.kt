package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase

class DefaultDependencies(context: Context) : Dependencies {
    val shoppingDatabase: ShoppingDatabase by lazy { ShoppingDatabase.getDatabase(context) }
    val cartProductDao: CartProductDao by lazy { shoppingDatabase.cartProductDao() }
    val productRepository: ProductRepository by lazy { DefaultProductRepository() }
    val cartRepository: CartRepository by lazy { DefaultCartRepository(cartProductDao) }
}
