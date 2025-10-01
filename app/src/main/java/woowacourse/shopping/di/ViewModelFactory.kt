package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val instance = DIContainer.instance.getInstance(modelClass.kotlin)

        @Suppress("UNCHECKED_CAST")
        return instance as T
    }

    companion object {
        val instance: ViewModelProvider.Factory by lazy { ViewModelFactory() }
    }
}
