package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
            modelClass.kotlin.primaryConstructor ?: return super.create(modelClass)
        val args: Map<KParameter, Any?> =
            constructor.parameters
                .associateWith { param ->
                    dependencies[param.type.classifier]
                        ?: if (param.isOptional) {
                            return@associateWith null
                        } else {
                            throw IllegalArgumentException("주입 불가: ${param.name} (${param.type})")
                        }
                }.filterValues { it != null }

        return constructor.callBy(args)
    }
}
