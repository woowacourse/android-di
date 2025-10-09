package woowacourse.shopping.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.di.DiContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class ViewModelDIFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val constructor =
            modelClass.kotlin.primaryConstructor ?: return create(modelClass)
        val args =
            constructor.parameters.associateWith { parameter ->
                when (val clazz = parameter.type.classifier as KClass<*>) {
                    SavedStateHandle::class -> extras.createSavedStateHandle()
                    else -> DiContainer.getProvider(clazz)
                }
            }
        return constructor.callBy(args) as T
    }
}
