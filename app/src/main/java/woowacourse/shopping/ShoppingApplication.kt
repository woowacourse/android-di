package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase

class ShoppingApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        val qualifier = RoomDB::class

        val cartProductDao: CartProductDao = when (qualifier) {
            InMemory::class -> InMemoryCartProductDao()
            else -> ShoppingDatabase.getInstance(this).cartProductDao()
        }

        DIContainer.register(ProductRepository::class, Remote::class) { DefaultProductRepository() }
        DIContainer.register(CartRepository::class, Remote::class) { DefaultCartRepository(cartProductDao) }
    }
}
