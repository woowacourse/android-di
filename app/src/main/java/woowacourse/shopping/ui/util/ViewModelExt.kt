package woowacourse.shopping.ui.util

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.di.ApiModule.createInstance

inline fun <reified VM : ViewModel> ComponentActivity.viewModels(): Lazy<VM> {
    return ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = {
            viewModelFactory { initializer { createInstance(VM::class.java) } }
        }
    )
}
