package woowacourse.shopping.util

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.di.DefaultViewModelModule

inline fun <reified VM : ViewModel> ComponentActivity.injectViewModel(): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        {
            viewModelFactory<VM>(applicationContext)
        },
    )
}

inline fun <reified VM : ViewModel> viewModelFactory(context: Context): ViewModelProvider.Factory =
    viewModelFactory {
        initializer {
            DefaultViewModelModule.getViewModelModule(context).createViewModel(VM::class.java)
        }
    }
