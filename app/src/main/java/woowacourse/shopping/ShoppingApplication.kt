package woowacourse.shopping

import android.app.Application
import com.boogiwoogi.di.version2.DefaultDiContainer
import com.boogiwoogi.di.version2.DiContainer
import com.boogiwoogi.di.version2.DiInjector
import com.boogiwoogi.di.version2.Modules
import woowacourse.shopping.di.application.ApplicationModule

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        container = DefaultDiContainer()
        modules = ApplicationModule(this)
        injector = DiInjector()
    }

    companion object {

        lateinit var container: DiContainer
        lateinit var modules: Modules
        lateinit var injector: DiInjector
    }
}
