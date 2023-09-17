package woowacourse.shopping.sangoondi

import woowacourse.shopping.sangoondi.annotation.Qualifier
import woowacourse.shopping.sangoondi.annotation.Singleton
import kotlin.reflect.KFunction
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

object DiContainer {
    private val _modules: MutableList<Any> = mutableListOf()
    val modules: List<Any> get() = _modules.toList()

    private val _singletons: MutableList<Any> = mutableListOf()
    val singletons: List<Any> get() = _singletons.toList()

    private val _qualifiedSingletons: MutableList<Any> = mutableListOf()
    val qualifiedSingletons: List<Any> get() = _qualifiedSingletons.toList()

    private lateinit var context: Any

    fun setContext(applicationContext: Any) {
        context = applicationContext
    }

    fun setModule(module: Any) {
        if (modules.contains(module)) return
        _modules.add(module)

        searchSingleton(module)
    }

    private fun searchSingleton(module: Any) {
        module::class.declaredFunctions.forEach { func ->
            if (func.hasAnnotation<Singleton>()) setSingleton(func)
        }
    }

    private fun setSingleton(func: KFunction<*>) {
        val instance = func.returnType.jvmErasure.createInstance()

        when (func.hasAnnotation<Qualifier>()) {
            true -> _qualifiedSingletons.add(instance)
            false -> _singletons.add(instance)
        }
    }
}
