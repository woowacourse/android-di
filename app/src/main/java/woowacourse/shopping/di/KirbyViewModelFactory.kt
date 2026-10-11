package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.di.KirbyDIContainer

class KirbyViewModelFactory(
    private val container: KirbyDIContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val scope = container.createScope()

        try {
            val viewModel = container.createInstance(modelClass.kotlin, scope)
            viewModel.addCloseable(SCOPE_KEY, scope)
            return viewModel
        } catch (error: Throwable) {
            scope.close()
            throw error
        }
    }

    companion object {
        internal const val SCOPE_KEY = "woowacourse.shopping.di.ViewModelScope"
    }
}
