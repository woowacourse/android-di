package woowacourse.shopping.ui.util

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.application.ShoppingApplication

inline fun <reified T : ViewModel> AppCompatActivity.createViewModel(): ViewModelLazy<T> {
    val factory = viewModelFactory {
        initializer {
            (application as ShoppingApplication).injector.create(T::class)
        }
    }
    return ViewModelLazy(
        T::class,
        { viewModelStore },
        { factory },
    )
}
