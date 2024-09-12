package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.DependencyInjector

class BaseViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    val di = DependencyInjector(appContainer)

    override fun <T : ViewModel> create(modelClass: Class<T>): T = di.createInstance(modelClass)
}
