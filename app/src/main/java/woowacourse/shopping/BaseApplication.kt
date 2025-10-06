package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.module.Module

abstract class BaseApplication : Application() {
    abstract fun setupModules()

    fun installModules(modules: List<Module>) {
        modules.forEach { it.register() }
    }
}
