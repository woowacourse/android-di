package woowacourse.shopping.application

import android.app.Application
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.di.RepositoryDependencyContainer

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DependencyInjector.repositoryDependency = RepositoryDependencyContainer()
    }
}
