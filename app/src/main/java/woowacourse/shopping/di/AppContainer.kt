package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class AppContainer(
    context: Context,
) {
    private val database: ShoppingDatabase by lazy {
        Room
            .databaseBuilder(
                context,
                ShoppingDatabase::class.java,
                "shopping.db",
            ).build()
    }

    private val cartProductDao by lazy { database.cartProductDao() }

    val productRepository: ProductRepository by lazy { ProductRepositoryImpl() }
    val cartRepository: CartRepository by lazy { CartRepositoryImpl(cartProductDao) }
}
