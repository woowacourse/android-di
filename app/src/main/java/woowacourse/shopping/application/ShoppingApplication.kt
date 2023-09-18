package woowacourse.shopping.application

import android.app.Application
import woowacourse.shopping.di.container.DefaultContainer
import woowacourse.shopping.di.container.DiContainer
import woowacourse.shopping.di.injector.Injector
import woowacourse.shopping.di.module.DefaultModule

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val module = DefaultModule(this)
        val appContainer: DiContainer = DefaultContainer()
        injector = Injector(appContainer)
        injector.addModuleInstances(module)
    }

    companion object {
        lateinit var injector: Injector
    }
}
