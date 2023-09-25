package woowacourse.shopping.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ssu.androidi.activity.DiActivity

inline fun <reified T : ViewModel> DiActivity.createViewModel(): ViewModelLazy<T> {
    return ViewModelLazy(
        T::class,
        { viewModelStore },
        {
            viewModelFactory {
                initializer {
                    injector.create(T::class)
                }
            }
        },
    )
}
