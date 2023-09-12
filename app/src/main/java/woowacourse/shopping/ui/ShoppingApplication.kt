package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.di.DefaultContainer
import woowacourse.shopping.data.di.Injector
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Injector.container = DefaultContainer
        initContainer()
    }

    private fun initContainer() {
        DefaultContainer.addInstance(ProductRepository::class, DefaultProductRepository())
        DefaultContainer.addInstance(
            CartProductDao::class,
            ShoppingDatabase.getDatabase(applicationContext).cartProductDao()
        )
        DefaultContainer.addInstance(CartRepository::class, Injector.inject<DefaultCartRepository>())
    }
}