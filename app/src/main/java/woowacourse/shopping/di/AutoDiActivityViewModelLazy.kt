package woowacourse.shopping.di

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.ui.MainApplication
import kotlin.reflect.KClass

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.autoViewModels(noinline extrasProducer: (() -> CreationExtras)? = null): Lazy<VM> {
    val factory =
        viewModelFactory {
            initializer {
                containerProvider(VM::class) as VM
            }
        }

    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        { factory },
        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras },
    )
}

fun CreationExtras.containerProvider(clazz: KClass<*>): Any? {
    val store = (this[APPLICATION_KEY] as MainApplication).appContainerStore
    return store.instantiate(clazz, saveToCache = false)
}
