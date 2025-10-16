package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase

class AppContainer(
    context: Context,
) {
    val cartProductDao: CartProductDao by lazy {
        ShoppingDatabase
            .getDatabase(
                context,
            ).cartProductDao()
    }

    val inMemoryCartRepository: CartRepository by lazy { InMemoryCartRepository() }

    val productRepository: ProductRepository by lazy { DefaultProductRepository() }
}
