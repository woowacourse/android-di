package woowacourse.shopping.util

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import woowacourse.shopping.di.auto.AppContainer
import woowacourse.shopping.di.auto.AutoInjectingViewModelFactory

inline fun <reified VM : ViewModel> AppCompatActivity.injectedViewModels(): Lazy<VM> =
    ViewModelLazy(
        VM::class,
        { viewModelStore },
        { AutoInjectingViewModelFactory(AppContainer) },
        { defaultViewModelCreationExtras },
    )
