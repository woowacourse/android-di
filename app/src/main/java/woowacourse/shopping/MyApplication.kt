package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.container.RepositoryContainer

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initContainer()
    }

    private fun initContainer() {
        repositoryContainer = RepositoryContainer()
    }

    companion object {
        lateinit var repositoryContainer: RepositoryContainer
    }
}
