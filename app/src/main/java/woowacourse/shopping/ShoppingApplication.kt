package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppInjector
import woowacourse.shopping.di.module.RepositoryModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppInjector.init(modules = listOf(RepositoryModule()))
    }
}
