package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

object DIViewModelFactory : ViewModelProvider.Factory by DependencyViewModelFactory()

internal class DependencyViewModelFactory : ViewModelProvider.Factory {
    private val dependencies: MutableMap<KClass<*>, Any> = mutableMapOf()

    override fun <T : ViewModel> create(modelClass: Class<T>): T = instantiate(modelClass.kotlin)

    private fun <T : Any> instantiate(type: KClass<T>): T {
        val constructor = type.primaryConstructor ?: error("주 생성자를 찾을 수 없습니다: ${type.qualifiedName}")
        val arguments =
            constructor.parameters.associateWith { parameter ->
                resolve(parameter.type.jvmErasure)
            }
        return constructor.callBy(arguments)
    }

    private fun resolve(type: KClass<*>): Any =
        dependencies.getOrPut(type) {
            instantiate(type)
        }
}
