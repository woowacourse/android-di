package woowacourse.shopping.ui.util

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ssu.androidi.application.DiApplication

inline fun <reified T : ViewModel> AppCompatActivity.createViewModel(): ViewModelLazy<T> {
    return ViewModelLazy(
        T::class,
        { viewModelStore },
        { viewModelFactory {
            initializer {
                (application as DiApplication).injector.create(T::class)
            }
        } },
    )
}
