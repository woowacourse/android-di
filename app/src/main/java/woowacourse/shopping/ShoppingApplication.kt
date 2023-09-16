package woowacourse.shopping

import com.mission.androiddi.component.application.InjectableApplication
import com.woowacourse.bunadi.dsl.modules
import com.woowacourse.bunadi.injector.SingletonDependencyInjector
import woowacourse.shopping.ui.common.di.module.DaoModule

class ShoppingApplication : InjectableApplication() {
    override fun onCreate() {
        super.onCreate()
        initModules()
    }

    private fun initModules() {
        SingletonDependencyInjector(this).modules {
            module(DaoModule(this@ShoppingApplication))
        }
    }
}
