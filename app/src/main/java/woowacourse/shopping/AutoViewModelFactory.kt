package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.di.FieldInjector
import com.example.di.ShoppingContainer
import com.example.di.ViewModelScoped

class AutoViewModelFactory(
    private val container: ShoppingContainer,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = modelClass.getDeclaredConstructor().newInstance()
        val scope = container.createViewModelScope()
        FieldInjector.inject(viewModel, scope)
        if (viewModel is ViewModelScoped) {
            viewModel.diScope = scope
        }
        return viewModel
    }
}
