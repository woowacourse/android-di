package woowacourse.shopping.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.di.DiContainer
import woowacourse.shopping.di.Injector
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class ViewModelDIFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val kClass = modelClass.kotlin
        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException()

        val arguments =
            constructor.parameters.associateWith { parameter ->
                val clazz = parameter.type.classifier as KClass<*>
                when (clazz) {
                    SavedStateHandle::class -> extras.createSavedStateHandle()
                    else -> DiContainer.getProvider(clazz)
                }
            }

        val vm = constructor.callBy(arguments)
        Injector.inject(vm)
        return vm
    }
}
