package woowacourse.shopping.di.auto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class AutoInjectingViewModelFactory(
    private val container: Container,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass = modelClass.kotlin
        val constructor =
            kClass.primaryConstructor
                ?: error("No primary constructor for ${kClass.qualifiedName}")

        val parameterValues =
            constructor.parameters
                .map { param ->
                    val kParam =
                        param.type.classifier as? KClass<*>
                            ?: error("Unsupported parameter type: ${param.type}")
                    container.get(kParam)
                }.toTypedArray()

        return constructor.call(*parameterValues)
    }
}
