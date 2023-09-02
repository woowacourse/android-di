package woowacourse.shopping.di.lazy

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import woowacourse.shopping.di.injector.ClassInjector
import woowacourse.shopping.di.util.viewModelFactory

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModel(): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        { viewModelFactory { createViewModel<VM>() } },
    )
}

@MainThread
inline fun <reified VM : ViewModel> createViewModel(): VM {
    return ClassInjector.inject()
}
