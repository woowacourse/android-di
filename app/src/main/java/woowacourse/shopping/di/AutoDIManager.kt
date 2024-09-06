package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

object AutoDIManager {
    private val dependencies: MutableMap<KClass<*>, Any> = mutableMapOf()

    fun registerDependency(
        type: KClass<*>,
        dependency: Any,
    ) {
        dependencies[type] = dependency
    }

    inline fun <reified VM : ViewModel> createViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                injectDependenciesWithParameters(VM::class)
            }
        }
    }

    fun <T : Any> injectDependenciesWithParameters(clazz: KClass<T>): T {
        val constructor =
            clazz.primaryConstructor ?: throw NullPointerException("주생성자에 파라미터가 없습니다.")

        val args =
            constructor.parameters.associateWith { parameter ->
                dependencies[parameter.type.jvmErasure]
            }

        return constructor.callBy(args)
    }
}
