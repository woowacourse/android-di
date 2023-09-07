package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.module
import woowacourse.shopping.di.startDI
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val db = Room.databaseBuilder(
            applicationContext,
            ShoppingDatabase::class.java, "cart_product"
        ).build()
        val dataModule = module {
            provide<ProductRepository> { ProductRepositoryImpl() }
            provide<CartRepository> { CartRepositoryImpl(db.cartProductDao()) }
        }
        startDI {
            loadModule(dataModule)
        }
    }
}
