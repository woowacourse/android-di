package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class AutoDIViewModelFactory(
    private val dependencies: Map<KClass<*>, Any>,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val constructor: KFunction<T> = modelClass.kotlin.constructors.first()
        val args: Array<Any> =
            constructor.parameters
                .map { param ->
                    dependencies[param.type.classifier]
                        ?: throw IllegalArgumentException("주입 불가: ${param.type}")
                }.toTypedArray()
        return constructor.call(*args)
    }
}
