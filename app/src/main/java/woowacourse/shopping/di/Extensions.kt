package woowacourse.shopping.di

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

inline fun <reified VM : ViewModel> AppCompatActivity.injectViewModels(): Lazy<VM> =
    viewModels {
        viewModelFactory {
            initializer {
                DIContainer.inject<VM>()
            }
        }
    }
