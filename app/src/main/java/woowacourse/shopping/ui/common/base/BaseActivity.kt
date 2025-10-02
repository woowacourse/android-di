package woowacourse.shopping.ui.common.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.DependencyProvider

abstract class BaseActivity : AppCompatActivity() {
    protected val viewModelFactory: ViewModelProvider.Factory
        get() = DependencyProvider.viewModelFactory
}
