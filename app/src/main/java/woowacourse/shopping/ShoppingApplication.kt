package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.DataModule
import woowacourse.shopping.di.inject.AutoDependencyInjector

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        localDatabase = ShoppingDatabase.getInstance(this)!!
        autoDependencyInjector = AutoDependencyInjector(DataModule)
    }

    companion object {
        lateinit var localDatabase: ShoppingDatabase
        lateinit var autoDependencyInjector: AutoDependencyInjector
    }
}
