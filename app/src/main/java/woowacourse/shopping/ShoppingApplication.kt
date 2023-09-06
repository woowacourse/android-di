package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DataModule
import woowacourse.shopping.di.inject.AutoDependencyInjector

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        autoDependencyInjector = AutoDependencyInjector(DataModule)
    }

    companion object {
        lateinit var autoDependencyInjector: AutoDependencyInjector
    }
}
