package woowacourse.shopping.ui.util

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import woowacourse.shopping.di.Injector

inline fun <reified VM : ViewModel> ComponentActivity.injectionViewModel(): VM {
    val viewModel: VM by viewModels {
        ViewModelFactory(
            viewModelClass = VM::class,
            instanceContainer = Injector.instanceContainer,
        )
    }
    return viewModel
}
