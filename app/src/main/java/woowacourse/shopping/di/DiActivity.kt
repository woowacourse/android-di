package woowacourse.shopping.di

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import woowacourse.shopping.ShoppingApplication

open class DiActivity : AppCompatActivity() {
    inline fun <reified T : ViewModel> DiActivity.viewModel() =
        viewModels<T> { ViewModelFactory((application as ShoppingApplication).diContainer) }
}
