package woowacourse.shopping.app

import android.app.Application
import woowacourse.shopping.data.repository.CartRepositoryImpl
import woowacourse.shopping.data.repository.ProductRepositoryImpl
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        addInstanceToContainer()
    }

    private fun addInstanceToContainer() {
        DIContainer.addInstance(ProductRepository::class, ProductRepositoryImpl())
        DIContainer.addInstance(CartRepository::class, CartRepositoryImpl())
    }
}
