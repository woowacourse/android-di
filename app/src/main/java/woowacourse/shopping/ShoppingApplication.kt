package woowacourse.shopping

import android.app.Application
import com.boogiwoogi.di.DefaultInstanceContainer
import com.boogiwoogi.di.DiInjector
import com.boogiwoogi.di.InstanceContainer
import com.boogiwoogi.di.Modules
import woowacourse.shopping.di.module.ShoppingApplicationModules

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        modules = ShoppingApplicationModules(this)
        container = DefaultInstanceContainer()
        injector = DiInjector()
    }

    companion object {

        lateinit var modules: Modules
        lateinit var container: InstanceContainer
        lateinit var injector: DiInjector
    }
}
