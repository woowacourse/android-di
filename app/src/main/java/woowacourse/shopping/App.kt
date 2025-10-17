package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.AppContainer.get
import woowacourse.shopping.di.qualifier.Qualifiers.DATABASE
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class App : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer
        appContainer.registerImplementation(CartRepository::class, CartRepositoryImpl::class)
        appContainer.registerImplementation(ProductRepository::class, ProductRepositoryImpl::class)
        appContainer.registerProvider(ShoppingDatabase::class, DATABASE) {
            ShoppingDatabase.getDatabase(context = applicationContext)
        }
        appContainer.registerProvider(CartProductDao::class, DATABASE) {
            get(ShoppingDatabase::class, DATABASE).cartProductDao()
        }
    }
}
