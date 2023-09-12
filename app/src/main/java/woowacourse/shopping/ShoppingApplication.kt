package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.Container
import woowacourse.shopping.di.Injector

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        database = ShoppingDatabase.getDatabase(this)

        Container.addInstance(DefaultProductRepository::class, null)
        Container.addInstance(DefaultCartRepository::class, null)
        Container.addInstance(
            CartProductDao::class,
            ShoppingDatabase.getDatabase(this).cartProductDao(),
        )

        injector = Injector(Container)
    }

    companion object {
        lateinit var injector: Injector
        lateinit var database: ShoppingDatabase
    }
}
