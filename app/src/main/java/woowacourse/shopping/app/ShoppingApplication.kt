package woowacourse.shopping.app

import android.app.Application
import woowacourse.shopping.data.repository.CartRepositoryImpl
import woowacourse.shopping.data.repository.ProductRepositoryImpl
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
        appContainer.addInstance(ProductRepository::class, ProductRepositoryImpl())
        appContainer.addInstance(CartRepository::class, CartRepositoryImpl())
    }

    companion object {
        lateinit var appContainer: AppContainer
            private set
    }
}
