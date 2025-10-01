package woowacourse.shopping.di

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel

inline fun <reified T : ViewModel> ComponentActivity.myDiViewModels() =
    viewModels<T> {
        ViewModelFactory.instance
    }
