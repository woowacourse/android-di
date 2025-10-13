package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.AppContainer.get
import woowacourse.shopping.di.AppContainer.registerImplementation
import woowacourse.shopping.di.AppContainer.registerProvider
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class App : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer
        registerImplementation(CartRepository::class, CartRepositoryImpl::class)
        registerImplementation(ProductRepository::class, ProductRepositoryImpl::class)
        registerProvider(ShoppingDatabase::class) {
            Room
                .databaseBuilder(
                    this.applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping-db",
                ).build()
        }
        registerProvider(CartProductDao::class) {
            get(ShoppingDatabase::class).cartProductDao()
        }
    }
}
