package woowacourse.shopping

import woowacourse.shopping.di.module.RepositoryModule

class ShoppingApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        setupModules()
    }

    override fun setupModules() {
        val modules = listOf(RepositoryModule())
        installModules(modules)
    }
}
