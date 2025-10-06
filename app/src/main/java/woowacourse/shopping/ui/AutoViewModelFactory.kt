package woowacourse.shopping.ui

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.savedstate.SavedStateRegistryOwner
import woowacourse.shopping.di.DiContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class AutoViewModelFactory(
    owner: SavedStateRegistryOwner,
) : AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): T {
        val kClass = modelClass.kotlin
        val constructor = requireNotNull(kClass.primaryConstructor) { kClass.java.simpleName }
        val params =
            constructor.parameters.associateWith { param ->
                when (param.type.classifier) {
                    SavedStateHandle::class -> handle
                    else -> DiContainer.getInstance(param.type.classifier as KClass<*>)
                }
            }
        return constructor.callBy(params)
    }
}

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.autoViewModel(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> Factory)? = { AutoViewModelFactory(this) },
): Lazy<VM> =
    ViewModelLazy(
        VM::class,
        { viewModelStore },
        factoryProducer ?: { defaultViewModelProviderFactory },
        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras },
    )
