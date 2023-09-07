package woowacourse.shopping.di.lazy

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import woowacourse.shopping.di.injector.DependencyInjector
import woowacourse.shopping.di.util.viewModelFactory

inline fun <reified VM : ViewModel> ComponentActivity.viewModel(): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        { viewModelFactory { createViewModel<VM>() } },
    )
}

inline fun <reified VM : ViewModel> createViewModel(): VM {
    return DependencyInjector.inject()
}
