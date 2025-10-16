package woowacourse.shopping

import android.app.Application
import com.example.di.DatabaseLogger
import com.example.di.InMemoryLogger
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class AppContainer(
    application: Application,
) {
    @InMemoryLogger
    private val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl()
    }

    @DatabaseLogger
    private val cartRepository: CartRepository by lazy {
        CartRepositoryImpl(cardDao)
    }

    private val cardDao: CartProductDao by lazy {
        database.cartProductDao()
    }

    private val database: ShoppingDatabase by lazy {
        ShoppingDatabase.getInstance(application)
    }
}
