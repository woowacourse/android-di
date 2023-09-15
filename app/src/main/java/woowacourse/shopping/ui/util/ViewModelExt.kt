package woowacourse.shopping.ui.util

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.re4rk.arkdiAndroid.DiAppCompatActivity

@MainThread
inline fun <reified VM : ViewModel> DiAppCompatActivity.viewModels(): Lazy<VM> {
    return ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = {
            viewModelFactory {
                initializer {
                    this@viewModels.injectViewModel(VM::class)
                }
            }
        },
    )
}
