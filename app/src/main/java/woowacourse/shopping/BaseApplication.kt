package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.di.module.Module

abstract class BaseApplication : Application() {
    val container = DIContainer()

    abstract fun setupModules()

    fun installModules(modules: List<Module>) {
        modules.forEach { it.register(container) }
    }
}
