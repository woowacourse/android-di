package woowacourse.shopping.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.di.injectViewModel
import woowacourse.shopping.di.openViewModelComponent
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

class ViewModelDIFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val k = modelClass.kotlin
        val ctor = k.primaryConstructor ?: error("No primary constructor: $k")
        val args = ctor.parameters
            .filter { it.kind == KParameter.Kind.VALUE }
            .associateWith { p ->
                when (p.type.classifier) {
                    SavedStateHandle::class -> extras.createSavedStateHandle()
                    else -> error("Only SavedStateHandle is allowed in $k constructor")
                }
            }
        @Suppress("UNCHECKED_CAST")
        return (ctor.callBy(args) as T).apply {
            openViewModelComponent()
            injectViewModel()
        }
    }
}

