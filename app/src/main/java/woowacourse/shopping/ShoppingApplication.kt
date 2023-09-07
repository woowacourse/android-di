package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.di.injector.modules
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val shoppingDatabase = Room.databaseBuilder(
            applicationContext,
            ShoppingDatabase::class.java,
            ShoppingDatabase.DATABASE_NAME,
        ).build()

        modules {
            inject<ProductRepository>(DefaultProductRepository())
            inject<CartRepository>(DefaultCartRepository(shoppingDatabase.cartProductDao()))
        }
    }
}
