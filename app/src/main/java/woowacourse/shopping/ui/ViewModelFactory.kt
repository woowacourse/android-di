package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.ViewModelScope
import woowacourse.shopping.appScopeKey
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.di.DIContainer.injectFields
import woowacourse.shopping.di.ScopeKey
import java.util.UUID
import kotlin.reflect.full.primaryConstructor

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = modelClass.kotlin.primaryConstructor ?: throw IllegalArgumentException("생성자를 찾을 수 없어요 : $modelClass")
        val viewModel = constructor.call()
        val scopeKey = ScopeKey(ViewModelScope, UUID.randomUUID().toString())
        DIContainer.openScope(scopeKey, parentKey = appScopeKey)

        try {
            injectFields(viewModel, scopeKey)
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
