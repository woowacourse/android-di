package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.local.ShoppingDatabase
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class RepositoryContainer(context: Context) : Container {
    private val shoppingDatabase = ShoppingDatabase.getDatabase(context)
    val productRepository: ProductRepository = DefaultProductRepository()
    val cartRepository: CartRepository = DefaultCartRepository(shoppingDatabase.cartProductDao())
}
