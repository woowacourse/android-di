package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class DependencyModule(
    context: Context,
) {
    private val shoppingDatabase: ShoppingDatabase = ShoppingDatabase.instance(context)

    @Dependency
    val productRepository: ProductRepository by lazy { DefaultProductRepository() }

    @Dependency
    val cartRepository: CartRepository by lazy { DefaultCartRepository(shoppingDatabase.cartProductDao()) }
}
