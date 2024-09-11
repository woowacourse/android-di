package woowacourse.shopping.di

import android.content.Context
import kotlin.reflect.KClass

class InjectionBuilder {
    private lateinit var context: Context
    private var modules = listOf<Any>()

    fun context(context: Context) {
        this.context = context
    }

    fun <T : Any> module(vararg modules: KClass<T>) {
        modules.forEach { module ->
            if (module.java.isAnnotationPresent(Module::class.java)) {
                this.modules += module.objectInstance ?: error("no object instance")
            }
        }
    }

    fun build(): InjectionData = InjectionData(modules, context)
}
