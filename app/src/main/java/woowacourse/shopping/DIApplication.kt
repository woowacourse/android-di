package woowacourse.shopping

import android.app.Application
import org.library.haeum.Container
import woowacourse.shopping.di.Module

class DIApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Container.initializeModuleInjector(this, Module::class)
    }
}
