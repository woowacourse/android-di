package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

object Injector {
    inline fun <reified T : ViewModel> inject(): T {
        val constructor = T::class.primaryConstructor
        requireNotNull(constructor) { "Unknown ViewModel Class ${T::class}" }

        val instances = constructor.parameters.map {
            Container.getInstance(it.type.jvmErasure)
        }

        return constructor.call(*instances.toTypedArray())
    }
}
