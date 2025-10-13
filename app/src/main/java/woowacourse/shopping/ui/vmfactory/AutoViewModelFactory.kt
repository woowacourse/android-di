package woowacourse.shopping.ui.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.FieldInjector
import woowacourse.shopping.ShoppingContainer

class AutoViewModelFactory(
    private val container: ShoppingContainer,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = modelClass.getDeclaredConstructor().newInstance()

        FieldInjector.inject(viewModel, container)

        return viewModel
    }
}
