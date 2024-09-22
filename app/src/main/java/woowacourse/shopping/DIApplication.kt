package woowacourse.shopping

import android.app.Application
import org.library.haeum.Container
import woowacourse.shopping.di.DIModule

class DIApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Container.initializeModuleInjector(this, DIModule::class)
    }
}
