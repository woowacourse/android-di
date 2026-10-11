package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.ViewModelScope
import woowacourse.shopping.appScopeKey
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.di.DependencyKey
import woowacourse.shopping.di.ScopeKey
import java.util.UUID

class ViewModelFactory(
    private val diContainer: DIContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val scopeKey = ScopeKey(ViewModelScope, UUID.randomUUID().toString())
        diContainer.openScope(scopeKey, parentKey = appScopeKey)

        try {
            val viewModel =
                diContainer.createInstance(
                    key = DependencyKey(modelClass.kotlin),
                    scopeKey = scopeKey,
                )
            viewModel.addCloseable {
                diContainer.closeScope(scopeKey)
            }
            return viewModel
        } catch (error: Throwable) {
            diContainer.closeScope(scopeKey)
            throw error
        }
    }
}
