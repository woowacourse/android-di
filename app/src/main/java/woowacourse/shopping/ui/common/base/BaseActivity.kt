package woowacourse.shopping.ui.common.base

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.AutoDIViewModelFactory

abstract class BaseActivity : AppCompatActivity() {
    protected val viewModelFactory: ViewModelProvider.Factory
        get() = AutoDIViewModelFactory()

    protected inline fun <reified VM : ViewModel> viewModels(): Lazy<VM> = viewModels { viewModelFactory }
}
