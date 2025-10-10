package woowacourse.shopping.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.createInstance

class ViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val kClass = modelClass.kotlin
        val vm = kClass.createInstance()
        val savedStateHandle = extras.createSavedStateHandle()

        // 3. 필드 주입 (lateinit var, var)
        kClass.members
            .filterIsInstance<KMutableProperty1<T, Any?>>()
            .forEach { prop ->
                val clazz = prop.returnType.classifier as? KClass<*>
                val dependency =
                    when (clazz) {
                        SavedStateHandle::class -> savedStateHandle
                        else -> clazz?.let { appContainer.resolve(it) }
                    }
                if (dependency != null) {
                    prop.setter.call(vm, dependency)
                }
            }

        return vm
    }
}
