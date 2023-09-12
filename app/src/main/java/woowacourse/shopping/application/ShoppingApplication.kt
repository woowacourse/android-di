package woowacourse.shopping.application

import android.app.Application
import woowacourse.shopping.data.database.DatabaseModule
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.di.StartInjection
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val db = DatabaseModule().providesShoppingDatabase(this)

        StartInjection {
            single<CartRepository>(DefaultCartRepository(db.cartProductDao()))
            single<ProductRepository>(DefaultProductRepository())
        }
    }
}
