package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.di.KirbyDIContainer

class KirbyViewModelFactory(
    private val container: KirbyDIContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = container.createInstance(modelClass.kotlin)
}
