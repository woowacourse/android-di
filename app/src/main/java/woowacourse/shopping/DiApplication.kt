package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DependencyFactory
import woowacourse.shopping.di.RepositoryContainer

class DiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        repositoryFactory = DependencyFactory(RepositoryContainer(applicationContext))
    }

    companion object {
        lateinit var repositoryFactory: DependencyFactory
    }
}
