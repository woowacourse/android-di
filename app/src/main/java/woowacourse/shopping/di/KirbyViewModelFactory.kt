package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class KirbyViewModelFactory(
    private val container: KirbyDIContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = container.createInstance(modelClass.kotlin)
}
