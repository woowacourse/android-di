package woowacourse.shopping.util

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.global.ShoppingApplication

fun AppCompatActivity.viewModelFactory() = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val application = application as ShoppingApplication
        return application.injector.inject(modelClass)
    }
}
