package woowacourse.shopping.di

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import woowacourse.shopping.ui.ShoppingApplication
import woowacourse.shopping.ui.util.ViewModelFactory

inline fun <reified VM : ViewModel> ComponentActivity.injectionViewModel(): VM {
    val viewModel: VM by viewModels {
        ViewModelFactory(
            viewModelClass = VM::class,
            instanceContainer = ShoppingApplication.instanceContainer
        )
    }
    return viewModel
}
