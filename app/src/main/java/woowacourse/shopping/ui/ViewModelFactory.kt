package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.ViewModelScope
import woowacourse.shopping.appScopeKey
import woowacourse.shopping.di.DependencyKey
import woowacourse.shopping.di.ScopeKey
import java.util.UUID
import kotlin.reflect.KClass

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T {
        val application =
            extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                as? ShoppingApplication
                ?: error("ShoppingApplication을 찾을 수 없습니다")
        val diContainer = application.diContainer

        val scopeKey = ScopeKey(ViewModelScope, UUID.randomUUID().toString())
        diContainer.openScope(scopeKey, parentKey = appScopeKey)

        try {
            val viewModel =
                diContainer.createInstance(
                    key = DependencyKey(modelClass),
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
