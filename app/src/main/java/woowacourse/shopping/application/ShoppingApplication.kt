package woowacourse.shopping.application

import android.app.Application
import woowacourse.shopping.di.DatabaseModule
import woowacourse.shopping.di.RepositoryModule
import woowacourse.shopping.di.sangoonDi.StartInjection
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val db = DatabaseModule.providesShoppingDatabase(this)

        StartInjection {
            single<CartRepository>(RepositoryModule.provideCartRepository(db.cartProductDao()))
            single<ProductRepository>(RepositoryModule.provideProductRepository())
        }
    }
}
