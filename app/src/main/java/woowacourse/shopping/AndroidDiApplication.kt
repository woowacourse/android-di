package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.di.DependencyRegistry
import woowacourse.shopping.di.RepositoryModule

class AndroidDiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeDependencies()
        injector = DependencyInjector(DependencyRegistry)
    }

    private fun initializeDependencies() {
        val repositoryModule = RepositoryModule()
        DependencyRegistry.initModule(repositoryModule)
    }

    companion object {
        lateinit var injector: DependencyInjector
    }
}
