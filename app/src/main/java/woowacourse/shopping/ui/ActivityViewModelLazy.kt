package woowacourse.shopping.ui

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModels(): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        { ShoppingViewModelFactory },
    )
}
