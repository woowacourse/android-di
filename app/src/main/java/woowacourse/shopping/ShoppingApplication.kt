package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.di.container.DependencyContainer
import woowacourse.shopping.di.inject.CustomInjector
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ShoppingApplication : Application() {

    lateinit var injector: CustomInjector
        private set

    override fun onCreate() {
        super.onCreate()
        injector = CustomInjector()
        DependencyContainer.setInstance(ProductRepository::class, injector.inject(DefaultProductRepository::class))
        DependencyContainer.setInstance(CartRepository::class, injector.inject(DefaultCartRepository::class))
    }
}
