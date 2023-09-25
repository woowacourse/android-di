package woowacourse.di

import android.app.Application
import woowacourse.di.module.AndroidModule

abstract class DIApplication(private val module: AndroidModule) : Application() {

    lateinit var diInjector: Injector
    override fun onCreate() {
        super.onCreate()

        val module = module.apply { setModuleContext(applicationContext) }
        val container = Container(module)
        diInjector = Injector(container)
    }
}
