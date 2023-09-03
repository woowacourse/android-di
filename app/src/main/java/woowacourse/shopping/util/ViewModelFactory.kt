package woowacourse.shopping.util

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.global.ShoppingApplication

fun ComponentActivity.viewModelFactory() = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val application = application as ShoppingApplication
        return application.injector.inject(modelClass)
    }
}
