package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sh1mj1.AppContainer
import com.example.sh1mj1.DependencyInjector

class BaseViewModelFactory(
    appContainer: AppContainer,
) : ViewModelProvider.Factory {
    private val dependencyInjector = DependencyInjector(appContainer)

    override fun <T : ViewModel> create(modelClass: Class<T>): T = dependencyInjector.createInstance(modelClass)
}
