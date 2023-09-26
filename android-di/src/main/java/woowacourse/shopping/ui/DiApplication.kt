package woowacourse.shopping.ui

import android.app.Application
import android.content.Context
import woowacourse.shopping.Injector
import woowacourse.shopping.container.DiContainer
import woowacourse.shopping.dslbuilder.ProviderBuilder

open class DiApplication : Application() {
    private val container: DiContainer by lazy { DiContainer() }
    val injector: Injector by lazy { Injector(container) }

    override fun onCreate() {
        super.onCreate()
        injector.addDependency(
            dependency = DEPENDENCY,
            clazz = Context::class,
            instance = this,
        )
    }

    fun registerProviders(block: ProviderBuilder.() -> Unit) {
        container.registerProviders(block)
    }

    companion object {
        private const val DEPENDENCY = "ApplicationContainer"
    }
}
