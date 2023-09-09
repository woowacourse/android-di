package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.common.AppContainer
import woowacourse.shopping.common.DefaultAppContainer
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        defaultAppContainer = DefaultAppContainer()
        addDefaultInstance(defaultAppContainer)
    }

    private fun addDefaultInstance(defaultAppContainer: AppContainer) {
        defaultAppContainer.addInstance(CartRepository::class, DefaultCartRepository())
        defaultAppContainer.addInstance(ProductRepository::class, DefaultProductRepository())
    }

    companion object {
        lateinit var defaultAppContainer: AppContainer
    }
}
