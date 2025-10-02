package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

class ViewModelFactory(
    private val dependencies: Map<KClass<*>, Any>,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass: KClass<T> = modelClass.kotlin
        val constructor: KFunction<T> =
            kClass.primaryConstructor ?: throw IllegalArgumentException()

        val args =
            constructor.parameters
                .map { param: KParameter ->
                    dependencies[param.type.classifier] ?: throw IllegalArgumentException()
                }.toTypedArray()
        return constructor.call(*args)
    }
}
