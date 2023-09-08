package woowacourse.shopping.di

import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.jvmErasure

class Injector(private val container: Container) {

    fun inject(module: Module) {
        module::class.declaredFunctions.forEach { func ->
            println(func.returnType.jvmErasure)
            func.call(module)?.let { container.setInstance(func.returnType.jvmErasure, it) }
        }
    }
}
