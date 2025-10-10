package woowacourse.shopping.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.ShoppingApplication

object ViewModelFactory {
    fun create(application: Application): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val diContainer = (application as ShoppingApplication).diContainer
                val viewModel: T = modelClass.getDeclaredConstructor().newInstance()
                diContainer.injectFields(viewModel)
                return viewModel
            }
        }
    }
}
