package woowacourse.shopping.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.AppViewModelFactory

abstract class BaseActivity : AppCompatActivity() {
    override val defaultViewModelProviderFactory: ViewModelProvider.Factory by lazy { AppViewModelFactory() }
}
