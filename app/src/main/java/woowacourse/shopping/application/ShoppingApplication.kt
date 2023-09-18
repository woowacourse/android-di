package woowacourse.shopping.application

import com.ssu.androidi.application.DiApplication
import woowacourse.shopping.di.module.ApplicationModule

class ShoppingApplication : DiApplication() {

    override fun onCreate() {
        super.onCreate()

        injector.addModuleInstances(ApplicationModule(this@ShoppingApplication))
    }
}
