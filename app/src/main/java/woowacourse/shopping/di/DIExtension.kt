package woowacourse.shopping.di

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.medandro.di.ViewModelFactory
import woowacourse.shopping.ShoppingApplication

inline fun <reified T : ViewModel> ComponentActivity.myDiViewModels() =
    viewModels<T> {
        ViewModelFactory.create((application as ShoppingApplication).diContainer)
    }
