package woowacourse.shopping.application

import android.app.Application
import woowacourse.shopping.di.container.DefaultContainer
import woowacourse.shopping.di.injector.Injector
import woowacourse.shopping.di.module.ApplicationModule

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        injector = Injector(DefaultContainer()).apply {
            addModuleInstances(ApplicationModule(this@ShoppingApplication))
        }
    }

    companion object {
        lateinit var injector: Injector
    }
}
