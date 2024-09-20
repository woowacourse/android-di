package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.module.ModuleLoader

class ShoppingApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val moduleLoader = ModuleLoader(this)
        moduleLoader.initializeModules()
    }
}
