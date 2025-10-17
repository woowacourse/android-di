package woowacouse.shopping.di

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import woowacouse.shopping.di.annotation.Inject
import woowacouse.shopping.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class ViewModelFactory(
    private val diFactory: DiFactory,
    private val handle: SavedStateHandle,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel: T =
            diFactory.create(
                modelClass.simpleName ?: "",
                modelClass.kotlin,
                handle = handle,
            )

        val field = modelClass.declaredFields.find { it.type == SavedStateHandle::class.java }
        field?.let {
            it.isAccessible = true
            it.set(viewModel, handle)
        }
        return viewModel as T
    }
}
