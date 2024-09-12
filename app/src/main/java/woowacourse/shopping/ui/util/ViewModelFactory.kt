package woowacourse.shopping.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import olive.di.DIContainer

class ViewModelFactory(private val diContainer: DIContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return diContainer.instance(modelClass.kotlin)
    }
}
