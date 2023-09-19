package woowacourse.shopping

import com.woowacourse.shopping.AndroidModule
import com.woowacourse.shopping.OtterDiApplication
import woowacourse.shopping.di.module.AppModule
import woowacourse.shopping.otterdi.Injector

class ShoppingApplication : OtterDiApplication() {

    override fun onCreate() {
        super.onCreate()
        initDependencies()
    }

    private fun initDependencies() {
        module = AppModule().apply { this.context = this@ShoppingApplication }
        injector = Injector(module)
    }

    companion object {
        lateinit var module: AndroidModule
        lateinit var injector: Injector
    }
}
