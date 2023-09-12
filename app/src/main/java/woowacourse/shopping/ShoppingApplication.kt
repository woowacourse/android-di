package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.Injector
import woowacourse.shopping.di.RepositoryContainer

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        database = ShoppingDatabase.getDatabase(this)

        val container = RepositoryContainer()
        container.addInstance(DefaultProductRepository::class, null)
        container.addInstance(DefaultCartRepository::class, null)
        container.addInstance(
            CartProductDao::class,
            ShoppingDatabase.getDatabase(this).cartProductDao(),
        )

        injector = Injector(container)
    }

    companion object {
        lateinit var injector: Injector
        lateinit var database: ShoppingDatabase
    }
}
