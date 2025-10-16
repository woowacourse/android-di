package woowacourse.shopping.di

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.App
import woowacourse.shopping.Container

class DIViewModelFactory(
    private val container: Container,
) : ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(
        modelClass: Class<VM>,
        extras: CreationExtras,
    ): VM = container.get(modelClass.kotlin)
}

inline fun <reified VM : ViewModel> ComponentActivity.injectViewModel(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null,
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: { DIViewModelFactory((application as App).container) }

    return ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = factoryPromise,
        extrasProducer = { extrasProducer?.invoke() ?: defaultViewModelCreationExtras },
    )
}
