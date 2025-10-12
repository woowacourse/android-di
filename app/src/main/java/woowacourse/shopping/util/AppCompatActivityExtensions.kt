package woowacourse.shopping.util

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import woowacourse.di.auto.AutoInjectingViewModelFactory
import woowacourse.shopping.di.AppContainer

inline fun <reified VM : ViewModel> AppCompatActivity.injectedViewModels(): Lazy<VM> =
    ViewModelLazy(
        VM::class,
        { viewModelStore },
        { AutoInjectingViewModelFactory(AppContainer) },
        { defaultViewModelCreationExtras },
    )
