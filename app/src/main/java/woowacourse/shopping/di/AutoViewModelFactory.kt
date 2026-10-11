package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.di.DependencyContainer
import woowacourse.shopping.ShoppingApplication

class AutoViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val scopeId =
            DependencyContainer.openChildScope(
                parentId = ShoppingApplication.APP_SCOPE_ID
            )

        return try {
            val viewModel =
                DependencyContainer.create(modelClass.kotlin, scopeId) as T

            val scopedViewModel =
                viewModel as? ScopedViewModel
                    ?: error("${modelClass.simpleName}은 ScopedViewModel을 상속해야 합니다.")

            scopedViewModel.attachScope(scopeId)
            viewModel
        } catch (error: Throwable) {
            DependencyContainer.closeScope(scopeId)
            throw error
        }
    }
}
