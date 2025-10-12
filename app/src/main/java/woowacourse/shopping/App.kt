package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        AppContainer.provide<CartProductDao>(ShoppingDatabase.getInstance(this).cartProductDao())
        AppContainer.provide<CartRepository>(DatabaseCartRepository::class)
        AppContainer.provide<ProductRepository>(InMemoryProductRepository::class)
    }
}
