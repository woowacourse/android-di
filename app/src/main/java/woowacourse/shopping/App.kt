package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.di.RepositoryModule
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        registerRepository()
    }

    private fun registerRepository() {
        DependencyInjector.register(ProductRepository::class) { RepositoryModule.provideProductRepository() }
        DependencyInjector.register(CartRepository::class) { RepositoryModule.provideCartRepository() }
    }
}
