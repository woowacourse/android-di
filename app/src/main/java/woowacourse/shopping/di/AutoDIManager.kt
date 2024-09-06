package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
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

    fun <T : ViewModel> createViewModelFactory(viewModelClass: KClass<T>): GenericViewModelFactory<ViewModel> {
        return GenericViewModelFactory(viewModelClass) {
            createInstanceWithParameters(viewModelClass)
                ?: error("Failed to create ViewModel instance")
        }
    }

    private fun <T : Any> createInstanceWithParameters(clazz: KClass<T>): T? {
        val constructor = clazz.primaryConstructor ?: return null
        val args =
            constructor.parameters.associateWith { parameter ->
                dependencies[parameter.type.jvmErasure]
            }

        return constructor.callBy(args)
    }
}
