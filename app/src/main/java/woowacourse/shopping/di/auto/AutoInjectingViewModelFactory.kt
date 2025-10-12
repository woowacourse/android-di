package woowacourse.shopping.di.auto

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class AutoInjectingViewModelFactory(
    private val container: Container,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val kClass = modelClass.kotlin
        val constructor =
            kClass.primaryConstructor
                ?: error("No primary constructor for ${kClass.qualifiedName}")

        val parameterValues =
            constructor.parameters
                .map { parameter ->
                    val kParameter =
                        parameter.type.classifier as? KClass<*>
                            ?: error("Unsupported parameter type: ${parameter.type}")

                    when (kParameter) {
                        SavedStateHandle::class -> extras.createSavedStateHandle()
                        else -> container.get(kParameter)
                    }
                }.toTypedArray()

        return constructor.call(*parameterValues)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = create(modelClass, CreationExtras.Empty)
}
