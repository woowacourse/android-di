package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.InjectedComponent

class ShoppingApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()

        container.add(
            InjectedComponent.InjectedSingletonComponent(ProductRepository::class, DefaultProductRepository()),
            InjectedComponent.InjectedSingletonComponent(
                CartRepository::class,
                DefaultCartRepository(CartProductDao.instance(this)),
            ),
        )
    }
}
