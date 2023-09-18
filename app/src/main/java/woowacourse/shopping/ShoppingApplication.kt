package woowacourse.shopping

import android.app.Application
import com.boogiwoogi.di.version2.DefaultInstanceContainer
import com.boogiwoogi.di.version2.DiInjector
import com.boogiwoogi.di.version2.InstanceContainer
import com.boogiwoogi.di.version2.Modules
import woowacourse.shopping.di.application.ApplicationModule

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        modules = ApplicationModule(this)
        container = DefaultInstanceContainer()
        injector = DiInjector()
    }

    companion object {

        lateinit var modules: Modules
        lateinit var container: InstanceContainer
        lateinit var injector: DiInjector
    }
}
