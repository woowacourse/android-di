package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.local.LocalCartRepository
import woowacourse.shopping.data.local.ShoppingDatabase
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.di.DependencyRegistry
import woowacourse.shopping.di.RepositoryModule
import woowacourse.shopping.di.RoomDBModule
import woowacourse.shopping.model.CartRepository

class AndroidDiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeDatabase()
        initializeDependencies()
    }

    private fun initializeDatabase() {
        ShoppingDatabase.init(this)
    }

    private fun initializeDependencies() {
        val roomDBModule = RoomDBModule()
        val repositoryModule = RepositoryModule()
        repositoryModule.addDependencies(CartRepository::class, LocalCartRepository::class)
        DependencyRegistry.initModule(repositoryModule, roomDBModule)
        injector = DependencyInjector(DependencyRegistry)
    }

    companion object {
        lateinit var injector: DependencyInjector
    }
}
