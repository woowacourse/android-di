package woowacourse.shopping.di

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import woowacourse.shopping.ShoppingApplication

inline fun <reified T : ViewModel> AppCompatActivity.viewModel() =
    viewModels<T> { ViewModelFactory(ShoppingApplication.diContainer) }
