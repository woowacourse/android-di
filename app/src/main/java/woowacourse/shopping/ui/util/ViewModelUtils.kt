package woowacourse.shopping.ui.util

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import com.example.androidi.application.DiApplication

inline fun <reified T : ViewModel> AppCompatActivity.createViewModel(factory: ViewModelProvider.Factory = ShoppingViewModelFactory): ViewModelLazy<T> {
    return ViewModelLazy(
        T::class,
        { viewModelStore },
        { factory },
    )
}

val ShoppingViewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DiApplication.injector.create(modelClass.kotlin)
    }
}
