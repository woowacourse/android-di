package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.AppDependencies

class ShoppingApplication :
    Application(),
    AppDependencies {
    override val cartDao: CartProductDao by lazy {
        Room
            .databaseBuilder(
                applicationContext,
                ShoppingDatabase::class.java,
                "shopping.db",
            ).build()
            .cartProductDao()
    }
    override val inMemoryCartRepository: CartRepository by lazy {
        InMemoryCartRepository()
    }

    override val roomCartRepository: CartRepository by lazy {
        DefaultCartRepository(cartDao)
    }

    override val productRepository: ProductRepository by lazy {
        DefaultProductRepository()
    }
}
