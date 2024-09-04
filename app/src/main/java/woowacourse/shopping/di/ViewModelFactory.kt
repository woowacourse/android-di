package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class ViewModelFactory(
    private val diContainer: DiContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val constructor =
            modelClass.kotlin.primaryConstructor
                ?: throw IllegalArgumentException("No primary constructor")

        val params =
            constructor.parameters.map { param ->
                diContainer.getInstance(param.type.jvmErasure)
            }
        return constructor.call(*params.toTypedArray())
    }
}
