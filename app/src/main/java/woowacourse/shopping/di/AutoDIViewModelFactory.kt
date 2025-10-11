package woowacourse.shopping.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

class AutoDIViewModelFactory(
    private val dependencies: Map<KClass<*>, Any>,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = create(modelClass, CreationExtras.Empty)

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val constructor: KFunction<T> =
            modelClass.kotlin.primaryConstructor
                ?: return super.create(modelClass)

        val args: Map<KParameter, Any?> =
            constructor.parameters
                .associateWith { param ->
                    when {
                        param.type.classifier == SavedStateHandle::class -> {
                            extras.savedStateHandle()
                        }

                        dependencies[param.type.classifier] != null -> {
                            dependencies[param.type.classifier]
                        }

                        param.isOptional -> null
                        else -> throw IllegalArgumentException("주입 불가: ${param.name}")
                    }
                }.filterValues { it != null }

        return constructor.callBy(args)
    }

    private fun CreationExtras.savedStateHandle(): SavedStateHandle = createSavedStateHandle()
}
