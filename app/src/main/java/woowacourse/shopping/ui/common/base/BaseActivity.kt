package woowacourse.shopping.ui.common.base

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import woowacourse.shopping.ShoppingApplication

abstract class BaseActivity : AppCompatActivity() {
    protected inline fun <reified VM : ViewModel> viewModels(): Lazy<VM> =
        viewModels { (application as ShoppingApplication).viewModelFactory }
}
