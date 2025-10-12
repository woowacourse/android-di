package woowacourse.shopping

import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.module.DatabaseModule
import woowacourse.shopping.di.module.RepositoryModule

class ShoppingApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        setupModules()
    }

    override fun setupModules() {
        val shoppingDatabase = ShoppingDatabase.getInstance(applicationContext)
        val modules = listOf(RepositoryModule(), DatabaseModule(shoppingDatabase))
        installModules(modules)
    }
}
