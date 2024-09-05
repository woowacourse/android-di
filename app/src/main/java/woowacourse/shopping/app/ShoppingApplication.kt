package woowacourse.shopping.app

import android.app.Application
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer()
        injectInstanceToContainer()
    }

    private fun injectInstanceToContainer() {
        appContainer.addInstance(ProductRepository::class, DefaultProductRepository())
        appContainer.addInstance(CartRepository::class, DefaultCartRepository())
    }

    companion object {
        lateinit var appContainer: AppContainer
            private set
    }
}
