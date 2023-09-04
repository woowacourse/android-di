package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.module.RepositoryModule

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initContainer()
    }

    private fun initContainer() {
        repositoryModule = RepositoryModule
    }

    companion object {
        lateinit var repositoryModule: RepositoryModule
    }
}
