package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase

class AppContainer(
    context: Context,
) {
    @RoomDB
    val roomCartRepository: CartRepository by lazy {
        DefaultCartRepository(
            ShoppingDatabase
                .getDatabase(
                    context,
                ).cartProductDao(),
        )
    }

    @InMemory
    val inMemoryCartRepository: CartRepository by lazy { InMemoryCartRepository() }

    val productRepository: ProductRepository by lazy { DefaultProductRepository() }
}
