package woowacourse.shopping

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

object ViewModelFactory {
    fun viewModelFactory(context: Context): ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = context.applicationContext as MyApplication
                val viewModel = app.appContainer.di.resolve(modelClass.kotlin) as T
                app.appContainer.di.injectFields(viewModel)
                return viewModel
            }
        }
}
