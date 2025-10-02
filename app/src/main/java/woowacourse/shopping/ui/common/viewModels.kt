package woowacourse.shopping.ui.common

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.viewmodel.CreationExtras

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> Factory)? = { DiViewModelFactory() },
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: { defaultViewModelProviderFactory }

    return ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = factoryPromise,
        extrasProducer = { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras }
    )
}
