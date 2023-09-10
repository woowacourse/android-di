package woowacourse.shopping.ui.util

import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.re4rk.arkdi.HasDiContainer

@MainThread
inline fun <reified VM : ViewModel> AppCompatActivity.viewModels(): Lazy<VM> {
    return ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = {
            viewModelFactory {
                initializer {
                    (this@viewModels as HasDiContainer).diContainer.createInstance(VM::class)
                }
            }
        },
    )
}
