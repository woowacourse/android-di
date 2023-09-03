package woowacourse.shopping.ui.util

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.di.DiActivity

@MainThread
inline fun <reified VM : ViewModel> DiActivity.viewModels(): Lazy<VM> {
    return ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = {
            viewModelFactory {
                initializer { this@viewModels.createInstance(VM::class.java) }
            }
        }
    )
}
