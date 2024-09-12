package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.local.ShoppingDatabase
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.di.DiContainer
import woowacourse.shopping.di.RepositoryModule
import woowacourse.shopping.di.RoomDBModule

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
        DiContainer.initModule(repositoryModule, roomDBModule)
        injector = DependencyInjector(DiContainer)
    }

    companion object {
        lateinit var injector: DependencyInjector
    }
}
