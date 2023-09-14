package woowacourse.shopping.application

import android.app.Application
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.annotations.StorageType
import woowacourse.shopping.di.container.DefaultContainer
import woowacourse.shopping.di.container.DiContainer
import woowacourse.shopping.di.injector.Injector
import woowacourse.shopping.repository.ProductRepository

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val appContainer: DiContainer = DefaultContainer()
        injector = Injector(appContainer)

        addInstancesToContainer(appContainer)
    }

    private fun addInstancesToContainer(
        appContainer: DiContainer,
    ) {
        val db: ShoppingDatabase = ShoppingDatabase.getInstance(this)
        appContainer.createInstance(CartProductDao::class, db.cartProductDao())
        appContainer.createInstance(
            ProductRepository::class,
            injector.create(DefaultProductRepository::class)
        )
        appContainer.createInstance(
            StorageType.DATABASE,
            injector.create(DefaultCartRepository::class)
        )
        appContainer.createInstance(
            StorageType.IN_MEMORY,
            injector.create(InMemoryCartRepository::class)
        )
    }

    companion object {
        lateinit var injector: Injector
    }
}
