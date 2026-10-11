package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.ViewModelScope
import woowacourse.shopping.appScopeKey
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.di.DependencyKey
import woowacourse.shopping.di.ScopeKey
import java.util.UUID

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val scopeKey = ScopeKey(ViewModelScope, UUID.randomUUID().toString())
        DIContainer.openScope(scopeKey, parentKey = appScopeKey)

        try {
            val viewModel =
                DIContainer.createInstance(
                    key = DependencyKey(modelClass.kotlin),
                    scopeKey = scopeKey,
                )
            viewModel.addCloseable {
                DIContainer.closeScope(scopeKey)
            }
            return viewModel
        } catch (error: Throwable) {
            DIContainer.closeScope(scopeKey)
            throw error
        }
    }
}
