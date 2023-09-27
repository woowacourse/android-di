package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.Injector
import woowacourse.shopping.Module
import woowacourse.shopping.container.DiContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

open class DiApplication : Application() {
    private val container: DiContainer by lazy { DiContainer() }
    val injector: Injector by lazy { Injector(container) }

    override fun onCreate() {
        super.onCreate()
    }

    fun <T : Any> registerModule(module: KClass<out T>) {
        injector.addModule(module.primaryConstructor?.call(this) as Module)
    }
}
