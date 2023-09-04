package woowacourse.shopping.ui.util

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.application.ShoppingApplication

inline fun <reified T : ViewModel> AppCompatActivity.createViewModel(clazz: Class<T>): T {
    val factory = viewModelFactory {
        initializer {
            (application as ShoppingApplication).injector.create(clazz)
        }
    }
    return ViewModelProvider(this, factory)[clazz]
}
