package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.Room
import woowacourse.shopping.data.ShoppingDatabase

class DefaultDependencies(context: Context) : Dependencies {

    private val shoppingDatabase: ShoppingDatabase by lazy { ShoppingDatabase.getDatabase(context) }

    @Room
    val cartProductDao: CartProductDao by lazy { shoppingDatabase.cartProductDao() }

    @Room
    val cartRepository: CartRepository by lazy { DefaultCartRepository(cartProductDao) }

    @Room
    val productRepository: ProductRepository by lazy { DefaultProductRepository() }
}
