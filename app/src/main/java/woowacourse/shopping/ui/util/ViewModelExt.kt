package woowacourse.shopping.ui.util

import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy

@MainThread
inline fun <reified VM : ViewModel> AppCompatActivity.viewModels(): Lazy<VM> {
    return ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = { (this as HasViewModelFactory).viewModelFactory },
    )
}
