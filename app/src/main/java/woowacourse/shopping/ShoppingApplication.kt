package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.container.RepositoryContainer

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        repositoryContainer = RepositoryContainer()
    }

    companion object {
        lateinit var repositoryContainer: RepositoryContainer
    }
}
