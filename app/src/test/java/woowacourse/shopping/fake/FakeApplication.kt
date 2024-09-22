package woowacourse.shopping.fake

import android.app.Application
import org.library.haeum.Container

class FakeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Container.initializeModuleInjector(this, FakeModule::class)
    }
}
