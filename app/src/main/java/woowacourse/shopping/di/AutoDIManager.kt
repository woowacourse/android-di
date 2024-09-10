package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

object AutoDIManager {
    val dependencies: MutableMap<KClass<*>, Any> = mutableMapOf()

    inline fun <reified T : Any> registerDependency(dependency: Any) {
        val clazz = T::class
        dependencies[clazz] = dependency
    }

    inline fun <reified VM : ViewModel> createViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                createAutoDIInstance<VM>()
            }
        }
    }

    inline fun <reified T : Any> createAutoDIInstance(): T {
        val clazz = T::class
        val constructor =
            clazz.primaryConstructor ?: throw NullPointerException("주생성자에 파라미터가 없습니다.")

        val args =
            constructor.parameters.associateWith { parameter ->
                dependencies[parameter.type.jvmErasure]
            }

        return constructor.callBy(args)
    }
}
