package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DependencyRegistry
import woowacourse.shopping.di.RepositoryModule

class AndroidDiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeDependencies()
    }

    private fun initializeDependencies() {
        val repositoryModule = RepositoryModule()
        DependencyRegistry.initModule(repositoryModule)
    }
}
