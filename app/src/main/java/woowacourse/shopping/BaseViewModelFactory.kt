package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sh1mj1.AppContainer
import com.example.sh1mj1.DependencyInjector

class BaseViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    val di = DependencyInjector(appContainer)

    override fun <T : ViewModel> create(modelClass: Class<T>): T = di.createInstance(modelClass)
}
