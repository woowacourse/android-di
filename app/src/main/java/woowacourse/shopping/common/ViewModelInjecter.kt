package woowacourse.shopping.common

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.viewmodel.CreationExtras

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModelInject(
    noinline extrasProducer: (() -> CreationExtras)? = null,
): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        { CommonViewModelFactory },
        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras },
    )
}
