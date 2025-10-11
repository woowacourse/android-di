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

class ViewModelDIFactory() : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val kClass = modelClass.kotlin
        val constructor = kClass.primaryConstructor
            ?: return create(modelClass)

        if (constructor is ViewModel) {
            Injector.inject(constructor)
            return constructor as T
        }

        val args = constructor.parameters.associateWith { parameter ->
            val clazz = parameter.type.classifier as KClass<*>
            when (clazz) {
                SavedStateHandle::class -> extras.createSavedStateHandle()
                else -> {
                    DiContainer.getProvider(clazz)
                }
            }
        }

        val vm = constructor.callBy(args)
        Injector.inject(vm)
        return vm
    }
}
