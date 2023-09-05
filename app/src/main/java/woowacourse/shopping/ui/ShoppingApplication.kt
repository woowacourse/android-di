package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.di.Container

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        inject()
    }

    private fun inject() {
        Container.addInstance(CartRepository::class, CartRepositoryImpl())
        Container.addInstance(ProductRepository::class, ProductRepositoryImpl())
    }
}
