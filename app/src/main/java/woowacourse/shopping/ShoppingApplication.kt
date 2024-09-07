package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository
import woowacourse.shopping.di.DIContainer

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setUpInstances()
    }

    private fun setUpInstances() {
        DIContainer.setInstance(
            ProductRepository::class,
            DefaultProductRepository(),
        )
        DIContainer.setInstance(
            CartRepository::class,
            DefaultCartRepository(),
        )
    }
}
