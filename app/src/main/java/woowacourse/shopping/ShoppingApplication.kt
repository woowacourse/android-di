package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.data.repository.InDiskCartRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.di.Container
import woowacourse.shopping.di.inject.AutoDependencyInjector.inject
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        localDatabase = ShoppingDatabase.getInstance(this)!!
        initContainer()
    }

    private fun initContainer() {
        Container.addInstance(CartProductDao::class, localDatabase.cartProductDao())
        Container.addInstance(ProductRepository::class, inject(DefaultProductRepository::class))
        Container.addInstance(CartRepository::class, inject(InDiskCartRepository::class))
        Container.addInstance(CartRepository::class, inject(InMemoryCartRepository::class))
    }

    companion object {
        lateinit var localDatabase: ShoppingDatabase
    }
}
