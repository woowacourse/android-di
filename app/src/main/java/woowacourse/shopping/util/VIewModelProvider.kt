package woowacourse.shopping.util

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.DefaultViewModelModule

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.injectViewModel(): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        {
            viewModelFactory(applicationContext)
        },
    )
}

fun viewModelFactory(context: Context): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DefaultViewModelModule(context).createViewModel(modelClass)
        }
    }
