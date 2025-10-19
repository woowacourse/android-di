package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val injector: DependencyInjector,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val scopeName: String = modelClass.simpleName
        injector.container.createScope(scopeName)

        val viewModel = injector.create(modelClass.kotlin, scopeName)
        viewModel.addCloseable { injector.container.clearScope(scopeName) }
        return viewModel
    }
}
